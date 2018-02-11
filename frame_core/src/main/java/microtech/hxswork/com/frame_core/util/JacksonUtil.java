package microtech.hxswork.com.frame_core.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import org.w3c.dom.Entity;

import java.io.IOException;
import java.util.List;

/**
 * jackson工具
 * @author 
 *
 */
public class JacksonUtil {

	private static final ObjectMapper mapper = new ObjectMapper();
	
	static {
		mapper.setSerializationInclusion(Include.NON_NULL);
	}
	
	/**
	 * 对象转json串
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static String generateJson(Object obj) {
		try {
			return mapper.writeValueAsString(obj);
		}catch (Exception e){

		}
	return null;
	}
	
	/**
	 * json串转对象
	 * @param <T>
	 * @param jsonStr
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	public static <T> T readJson(String jsonStr, Class<T> clazz) throws IOException {
		if (jsonStr == null || jsonStr.equals(""))
			return null;
		return mapper.readValue(jsonStr, clazz);
	}
	
	/**
	 * json数组串转list
	 * @param <T>
	 * @param arrayStr
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	public static <T> List<T> readList(String arrayStr, Class<T> clazz) throws IOException {
//		JsonNode arrayNode = mapper.readTree(arrayStr);
//		if(arrayNode.isMissingNode() || !arrayNode.isArray())
//			return null;
		
		CollectionType type = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
		return mapper.readValue(arrayStr, type);
	}
	public static void main(String[] args) throws IOException {
	
	}
}
