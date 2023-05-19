package common.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module.Feature;
import com.google.gson.JsonObject;

public class JsonUtils {
	private static final Logger logger = Logger.getLogger(JsonUtils.class);
	private static ObjectMapper objectMapper;
	static {
		objectMapper = objectMapper2();

	}

	public static JSONObject parse(String jsonStr) {
		try {
			return new JSONObject(jsonStr);
		} catch (JSONException e) {
			return null;
		}
	}

	private static final ThreadLocal<Set<Object>> context = new ThreadLocal<Set<Object>>();

	@SuppressWarnings("unused")
	private static boolean serialized(Object object) {
		if (context.get() == null) {
			HashSet<Object> hs = new HashSet<Object>();
			context.set(hs);
			hs.add(object);
			return false;
		}
		HashSet<Object> hs = (HashSet<Object>) context.get();
		if (hs.contains(object))
			return true;
		hs.add(object);
		return false;
	}

	public static ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper()
				.registerModule(new SimpleModule().addSerializer(Date.class, new JsonSerializer<Date>() {
					@Override
					public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
							throws IOException, JsonProcessingException {
						if (date == null) {
							jsonGenerator.writeNull();
						} else {
							jsonGenerator.writeObject(Formater.date2str(date));
						}
					}
				}).addSerializer(HibernateProxy.class, new JsonSerializer<HibernateProxy>() {
					@Override
					public void serialize(HibernateProxy proxy, JsonGenerator jsonGenerator,
							SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
						if (proxy == null) {
							jsonGenerator.writeNull();
						} else {
							HibernateProxy hibernateProxy = (HibernateProxy) proxy;
							LazyInitializer initializer = hibernateProxy.getHibernateLazyInitializer();
							Object obj = null;
							try {
								obj = initializer.getImplementation();
							} catch (org.hibernate.LazyInitializationException e) {
								jsonGenerator.writeNull();
								return;
							}
							if (obj == null)
								jsonGenerator.writeNull();
							else
								jsonGenerator.writeObject(obj);
						}
					}
				}))/*
					 * .registerModule(new SimpleModule().addSerializer(JavassistLazyInitializer.class, new
					 * JsonSerializer<JavassistLazyInitializer>() {
					 * 
					 * @Override public void serialize(JavassistLazyInitializer proxy, JsonGenerator jsonGenerator,
					 * SerializerProvider serializerProvider) throws IOException, JsonProcessingException { if (proxy ==
					 * null) { jsonGenerator.writeNull(); } else { Object obj = proxy.getImplementation();
					 * jsonGenerator.writeObject(obj); } } }))
					 */;
		return objectMapper;
	}

	public static ObjectMapper objectMapper1() {
		ObjectMapper objectMapper = new ObjectMapper()
				.registerModule(new SimpleModule().addSerializer(Date.class, new JsonSerializer<Date>() {
					@Override
					public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
							throws IOException, JsonProcessingException {
						if (date == null) {
							jsonGenerator.writeNull();
						} else {
							jsonGenerator.writeObject(Formater.date2str(date));
						}
					}
				}).addSerializer(HibernateProxy.class, new JsonSerializer<HibernateProxy>() {
					@Override
					public void serialize(HibernateProxy proxy, JsonGenerator jsonGenerator,
							SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
						if (proxy == null) {
							jsonGenerator.writeNull();
						} else {
							HibernateProxy hibernateProxy = (HibernateProxy) proxy;
							LazyInitializer initializer = hibernateProxy.getHibernateLazyInitializer();
							Object obj = null;
							try {
								obj = initializer.getImplementation();
							} catch (org.hibernate.LazyInitializationException e) {
								jsonGenerator.writeNull();
								return;
							}
							if (obj == null)
								jsonGenerator.writeNull();
							else
								jsonGenerator.writeObject(obj);
						}
					}
				}).addSerializer(Timestamp.class, new JsonSerializer<Timestamp>() {
					@Override
					public void serialize(Timestamp timestamp, JsonGenerator jsonGenerator,
							SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
						if (timestamp == null) {
							jsonGenerator.writeNull();
						} else {
							jsonGenerator.writeObject(Formater.dateTime2str(timestamp));
						}
					}
				}).addDeserializer(Timestamp.class, new JsonDeserializer<Timestamp>() {
					@Override
					public Timestamp deserialize(JsonParser jp, DeserializationContext ctxt)
							throws IOException, JsonProcessingException {
						String timestamp = jp.getText().trim();
						try {
							return new Timestamp(Formater.str2DateTime(timestamp).getTime());
						} catch (Exception e) {
							logger.warn("Unable to deserialize timestamp: " + timestamp, e);
							return null;
						}
					}

				}).addDeserializer(Date.class, new JsonDeserializer<Date>() {
					@Override
					public Date deserialize(JsonParser jp, DeserializationContext ctxt)
							throws IOException, JsonProcessingException {
						String date = jp.getText().trim();
						try {
							return Formater.str2date(date);
						} catch (Exception e) {
							logger.warn("Unable to deserialize timestamp: " + date, e);
							return null;
						}
					}
				}).addSerializer(BigDecimal.class, new JsonSerializer<BigDecimal>() {
					@Override
					public void serialize(BigDecimal num, JsonGenerator jsonGenerator,
							SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
						if (num == null) {
							jsonGenerator.writeNull();
						} else {
							jsonGenerator.writeObject(FormatNumber.num2Str(num));
						}
					}
				}).addSerializer(Long.class, new JsonSerializer<Long>() {
					@Override
					public void serialize(Long num, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
							throws IOException, JsonProcessingException {
						if (num == null) {
							jsonGenerator.writeNull();
						} else {
							jsonGenerator.writeObject(Formater.num2str(num));
						}
					}
				}).addSerializer(Integer.class, new JsonSerializer<Integer>() {
					@Override
					public void serialize(Integer num, JsonGenerator jsonGenerator,
							SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
						if (num == null) {
							jsonGenerator.writeNull();
						} else {
							jsonGenerator.writeObject(Formater.num2str(num));
						}
					}
				}));
		return objectMapper;
	}
	public static ObjectMapper objectMapper2() {
		ObjectMapper objectMapper = new ObjectMapper()
				.registerModule(new SimpleModule().addSerializer(Date.class, new JsonSerializer<Date>() {
					@Override
					public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
							throws IOException, JsonProcessingException {
						if (date == null) {
							jsonGenerator.writeNull();
						} else {
							jsonGenerator.writeObject(Formater.date2str(date));
						}
					}
				}).addSerializer(Timestamp.class, new JsonSerializer<Timestamp>() {
					@Override
					public void serialize(Timestamp timestamp, JsonGenerator jsonGenerator,
							SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
						if (timestamp == null) {
							jsonGenerator.writeNull();
						} else {
							jsonGenerator.writeObject(Formater.dateTime2str(timestamp));
						}
					}
				}).addDeserializer(Timestamp.class, new JsonDeserializer<Timestamp>() {
					@Override
					public Timestamp deserialize(JsonParser jp, DeserializationContext ctxt)
							throws IOException, JsonProcessingException {
						String timestamp = jp.getText().trim();
						try {
							return new Timestamp(Formater.str2DateTime(timestamp).getTime());
						} catch (Exception e) {
							logger.warn("Unable to deserialize timestamp: " + timestamp, e);
							return null;
						}
					}

				}).addDeserializer(Date.class, new JsonDeserializer<Date>() {
					@Override
					public Date deserialize(JsonParser jp, DeserializationContext ctxt)
							throws IOException, JsonProcessingException {
						String date = jp.getText().trim();
						try {
							return Formater.str2date(date);
						} catch (Exception e) {
							logger.warn("Unable to deserialize timestamp: " + date, e);
							return null;
						}
					}
				}).addSerializer(BigDecimal.class, new JsonSerializer<BigDecimal>() {
					@Override
					public void serialize(BigDecimal num, JsonGenerator jsonGenerator,
							SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
						if (num == null) {
							jsonGenerator.writeNull();
						} else {
							jsonGenerator.writeObject(FormatNumber.num2Str(num));
						}
					}
				}).addSerializer(Long.class, new JsonSerializer<Long>() {
					@Override
					public void serialize(Long num, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
							throws IOException, JsonProcessingException {
						if (num == null) {
							jsonGenerator.writeNull();
						} else {
							jsonGenerator.writeObject(Formater.num2str(num));
						}
					}
				}).addSerializer(Integer.class, new JsonSerializer<Integer>() {
					@Override
					public void serialize(Integer num, JsonGenerator jsonGenerator,
							SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
						if (num == null) {
							jsonGenerator.writeNull();
						} else {
							jsonGenerator.writeObject(Formater.num2str(num));
						}
					}
				}));
		


		Hibernate5Module hbm = new Hibernate5Module();
		
		
		
		hbm.enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);
		//hbm.enable(Hibernate5Module.Feature.REPLACE_PERSISTENT_COLLECTIONS);
		hbm.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);
		
		
		objectMapper = objectMapper.registerModule(hbm);
		objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
		return objectMapper;
	}
	public static String writeToString(Object obj) throws JsonProcessingException {
		return objectMapper.writeValueAsString(obj);
	}

	public static String writeToString(Object obj, Boolean pretty) throws JsonProcessingException {
		if (pretty == null || !pretty)
			return writeToString(obj);
		return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
	}

	public static void writeToStream(Object obj, OutputStream out)
			throws JsonGenerationException, JsonMappingException, IOException {
		objectMapper.writeValue(out, obj);
	}

	public static void writeToWriter(Object obj, Writer out)
			throws JsonGenerationException, JsonMappingException, IOException {
		objectMapper.writeValue(out, obj);
	}

	public static void writeToResponse(Object obj, HttpServletResponse rs)
			throws JsonGenerationException, JsonMappingException, IOException {
		rs.setContentType("application/json;charset=utf-8");
		rs.setCharacterEncoding("utf-8");
		rs.setHeader("Cache-Control", "no-store");
		if (obj instanceof JsonObject || obj instanceof JSONObject) {
			Writer w = rs.getWriter();
			w.write(obj.toString());
			w.flush();
			w.close();
		} else {
			OutputStream os = rs.getOutputStream();
			objectMapper.writeValue(os, obj);
			//objectMapper2().writeValue(os, obj);
			os.flush();
			os.close();
		}

	}
}
