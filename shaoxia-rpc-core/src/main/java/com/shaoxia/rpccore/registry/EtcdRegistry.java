package com.shaoxia.rpccore.registry;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import com.shaoxia.rpccore.config.RegistryConfig;
import com.shaoxia.rpccore.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.watch.WatchEvent;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wjc28
 * @version 1.0
 * @description: TODO
 * @date 2024-06-28 12:39
 */
@Slf4j
public class EtcdRegistry implements Registry{
	private Client client;

	private KV kvClient;

	private final Set<String> localRegisterNodeKeySet = new HashSet<>();

	private static final String ETCD_ROOT_PATH = "/rpc/";

	private final RegistryServiceCache registryServiceCache = new RegistryServiceCache();

	/**
	 * 正在监听的节点集合
	 */
	private final Set<String> watchingNodeSet = new ConcurrentHashSet<>();

	@Override
	public void init(RegistryConfig registryConfig) {
		client = Client.builder().endpoints(registryConfig.getRegistryAddress())
				.connectTimeout(Duration.ofMillis(registryConfig.getTimeout()))
				.build();
		kvClient = client.getKVClient();
		heartBeat();
	}

	@Override
	public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
		Lease leaseClient = client.getLeaseClient();

		long leaseId = leaseClient.grant(30).get().getID();

		// 设置要存储的键值对
		String registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
		ByteSequence key = ByteSequence.from(registerKey, StandardCharsets.UTF_8);
		ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

		// 把键值对和租约关联起来
		PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
		kvClient.put(key,value,putOption).get();
		localRegisterNodeKeySet.add(registerKey);
	}

	@Override
	public void unRegister(ServiceMetaInfo serviceMetaInfo) {
		String key = serviceMetaInfo.getServiceKey();
		kvClient.delete(ByteSequence.from(ETCD_ROOT_PATH+serviceMetaInfo.getServiceNodeKey(),StandardCharsets.UTF_8));
		localRegisterNodeKeySet.remove(key);
	}

	@Override
	public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
		List<ServiceMetaInfo> serviceMetaInfosInCache = registryServiceCache.readCache();
		if (serviceMetaInfosInCache != null){
			return serviceMetaInfosInCache;
		}

		// 前缀搜索
		String searchPrefix = ETCD_ROOT_PATH + serviceKey + "/";

		try {
			GetOption getOption = GetOption.builder().isPrefix(true).build();
			List<KeyValue> kvs = kvClient.get(
							ByteSequence.from(searchPrefix, StandardCharsets.UTF_8),
							getOption
					).get()
					.getKvs();
			List<ServiceMetaInfo> serviceMetaInfos = kvs.stream().map(keyValue -> {
				// 监听服务
				String key = keyValue.getKey().toString(StandardCharsets.UTF_8);
				watch(key);
				String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
				return JSONUtil.toBean(value, ServiceMetaInfo.class);
			}).collect(Collectors.toList());
			// 写入服务缓存
			registryServiceCache.writeCache(serviceMetaInfos);
			return serviceMetaInfos;
		}catch (Exception e){
			throw new RuntimeException("获取服务列表失败",e);
		}
	}

	@Override
	public void destroy() {
		System.out.println("当前节点下线");
		// 下线所有节点
		for (String registerKey : localRegisterNodeKeySet) {
			try {
				kvClient.delete(ByteSequence.from(registerKey,StandardCharsets.UTF_8));
			}catch (Exception e){
				throw new RuntimeException(registerKey + "节点下线失败");
			}
		}
		if (kvClient != null) {
			kvClient.close();
		}
		if (client != null){
			client.close();
		}

	}

	@Override
	public void heartBeat() {
		log.info("heartBeat once--");
		CronUtil.schedule("*/10 * * * * *", (Task) () -> {
			log.info("--- heart beat ---");
			for (String registryKey : localRegisterNodeKeySet) {
				try {
					List<KeyValue> kvs = kvClient.get(ByteSequence.from(registryKey, StandardCharsets.UTF_8))
							.get()
							.getKvs();
					if (CollUtil.isEmpty(kvs)){
						continue;
					}
					KeyValue keyValue = kvs.get(0);
					String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
					ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);
					register(serviceMetaInfo);
				} catch (Exception e) {
					throw new RuntimeException("续签失败",e);
				}
			}
		});

		CronUtil.setMatchSecond(true);
		CronUtil.start();

	}

	/**
	 * 监听（消费端）
	 * @param watchNodeKey 监听的服务Key
	 */
	@Override
	public void watch(String watchNodeKey) {
		Watch watchClient = client.getWatchClient();
		// 添加新的监听
		boolean newWatch = watchingNodeSet.add(watchNodeKey);
		if (newWatch){
			watchClient.watch(ByteSequence.from(watchNodeKey,StandardCharsets.UTF_8),watchResponse -> {
				for (WatchEvent event : watchResponse.getEvents()) {
					switch (event.getEventType()){
						// key 删除时触发
						case DELETE:
							registryServiceCache.clear();
							break;
						case PUT:
						default:
							break;
					}
				}
			});
		}

	}
}
