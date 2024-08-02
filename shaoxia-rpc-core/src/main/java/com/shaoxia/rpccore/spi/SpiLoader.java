package com.shaoxia.rpccore.spi;

import cn.hutool.core.io.resource.ResourceUtil;
import com.shaoxia.rpccore.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wjc28
 * @version 1.0
 * @description: SPI 映射 支持对键的映射
 * @date 2024-06-26 16:03
 */
@Slf4j
public class SpiLoader {
	/**
	 * 存储已经加载的类 接口名 -> （key => 实现类）
	 */
	private static Map<String ,Map<String ,Class<?> >> loadMap = new ConcurrentHashMap<>();


	/**
	 * 对象实例缓存
	 */
	private static final Map<String,Object> instanceCache = new ConcurrentHashMap<>();

	/**
	 * 系统SPI目录
	 */
	private static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system/";

	/**
	 * 用户自定义SPI目录
	 */
	private static final String RPC_CUSTOM_SPI_DIR = "META-INF/rpc/custom/";

	/**
	 * 扫描路径
	 */
	private static final String[] SCAN_DIRS = new String[]{RPC_SYSTEM_SPI_DIR, RPC_CUSTOM_SPI_DIR};

	/**
	 * 关联动态加载的类列表
	 */
	private static final List<Class<?>> LOAD_CLASS_LIST = Collections.singletonList(Serializer.class);

	/**
	 * 加载所有类型
	 */
	public static void loadAll(){
		log.info("加载所有SPI");
		for (Class<?> aClass : LOAD_CLASS_LIST) {
			load(aClass);
		}
	}

	/**
	 * 加载某个类
	 * @param loadClass 要加载的类
	 * @return
	 */
	public static Map<String,Class<?>> load(Class<?> loadClass){
		log.info("加载类型为 {} 的 SPI",loadClass.getName());

		// 扫描路径，用户自定义SPI 优先级高于系统SPI
		Map<String,Class<?>> keyClassMap = new HashMap<>();

		// 扫描用户和系统的配置
		for (String scanDir : SCAN_DIRS) {
			// 扫描每一个符合类名的资源文件
			List<URL> resources = ResourceUtil.getResources(scanDir + loadClass.getName());
			for (URL resource : resources) {
				try {
					InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
					BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						String[] strArray = line.split("=");
						if (strArray.length > 1) {
							String key = strArray[0];
							String className = strArray[1];
							keyClassMap.put(key,Class.forName(className));
						}
					}
				} catch (Exception e) {
					log.error("spi resource load error",e);
				}
			}
		}
		loadMap.put(loadClass.getName(),keyClassMap);
		return keyClassMap;
	}

	/**
	 * 从缓存里获取实例
	 * @param tClass
	 * @param key
	 * @return
	 * @param <T>
	 */
	public static <T> T getInstance(Class<?> tClass,String key){
		String tClassName = tClass.getName();
		Map<String, Class<?>> KeyImpClassMap = loadMap.get(tClassName);
		if (KeyImpClassMap == null) {
			throw new RuntimeException(String.format("SpiLoader 未加载 %s 类 ",tClassName));
		}
		if (!KeyImpClassMap.containsKey(key)){
			throw new RuntimeException(String.format("SpiLoader的%s类不存在key为%s的实现类",tClassName,key));
		}
		// 获取要加载的实现类型
		Class<?> impClass = KeyImpClassMap.get(key);
		// 从实例缓存里加载指定类型
		String impClassName = impClass.getName();
		if (!instanceCache.containsKey(impClassName)){
			try {
				instanceCache.put(impClassName,impClass.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				String errorMessage = String.format("%s 类实例化失败",impClassName);
				throw new RuntimeException(errorMessage,e);
			}
		}
		return (T)instanceCache.get(impClassName);
	}


}
