package frwk.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import common.util.FormatNumber;

public class Utils {
	private static final Logger logger = Logger.getLogger(Utils.class);

//	public static boolean isVtbHo(Partner p) {
//		return "CIC".equals(p.getCode());
//	}
//
//	public static boolean isVtbHo(SysUsers su) {
//		return isVtbHo(su.getCompany());
//	}

	private static final ObjectMapper objectMapper2 = new ObjectMapper()
			.registerModule(new SimpleModule().addSerializer(Date.class, new JsonSerializer<Date>() {
				@Override
				public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
						throws IOException, JsonProcessingException {
					if (date == null) {
						jsonGenerator.writeNull();
					} else {
						DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
						// You can set timezone however you wish
						// df.setTimeZone(TimeZone.getDefault());
						jsonGenerator.writeObject(df.format(date));
					}
				}
			}).addSerializer(String.class, new JsonSerializer<String>() {
				@Override
				public void serialize(String date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
						throws IOException, JsonProcessingException {
					if (date == null) {
						jsonGenerator.writeNull();
					} else {
						jsonGenerator.writeString(date);
					}
				}
			}).addSerializer(BigDecimal.class, new JsonSerializer<BigDecimal>() {
				@Override
				public void serialize(BigDecimal date, JsonGenerator jsonGenerator,
						SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
					if (date == null) {
						jsonGenerator.writeNull();
					} else {
						jsonGenerator.writeString(FormatNumber.num2Str(date));
					}
				}
			}));
	private static final ObjectMapper objectMapper = new ObjectMapper()
			.setDateFormat(new SimpleDateFormat("dd/MM/yyyy"));
	private static final ObjectMapper objectMapper1 = new ObjectMapper().registerModule(
			new SimpleModule("isoDate", Version.unknownVersion()).addSerializer(Date.class, new JsonSerializer<Date>() {
				@Override
				public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
						throws IOException, JsonProcessingException {
					if (date == null) {
						jsonGenerator.writeNull();
					} else {
						DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
						// You can set timezone however you wish
						// df.setTimeZone(TimeZone.getDefault());
						jsonGenerator.writeObject(df.format(date));
					}
				}
			}).addDeserializer(Date.class, new JsonDeserializer<Date>() {
				@Override
				public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
						throws IOException, JsonProcessingException {
					return null;
				}
			}));

	public static void jsonSerialize(HttpServletResponse response, Object obj) throws Exception {
		try {
			response.setContentType("application/json;charset=utf-8");
			response.setHeader("Cache-Control", "no-store");
			PrintWriter out = response.getWriter();
			JSONObject jsonObject = new JSONObject(objectMapper2.writeValueAsString(obj));
			out.print(jsonObject);
			out.close();
		} catch (Exception ex) {
			logger.error("Loi", ex);
			throw ex;
		}

	}

	public static void returnObject(HttpServletResponse response, Object obj) throws Exception {
		try {
			response.setContentType("text/html;charset=utf-8");
			response.setHeader("Cache-Control", "no-store");
			PrintWriter out = response.getWriter();
			JSONObject jsonObject = new JSONObject(new ObjectMapper().writeValueAsString(obj));
			out.print(jsonObject);
			out.close();
		} catch (Exception ex) {
			logger.error("Loi", ex);
			throw ex;
		}

	}

	public static void jsonSerializeRoot(HttpServletResponse response, String rootName, Object obj) throws Exception {
		try {
			response.setContentType("application/json;charset=utf-8");
			response.setHeader("Cache-Control", "no-store");
			PrintWriter out = response.getWriter();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(rootName, new JSONObject(new ObjectMapper().writeValueAsString(obj)));
			out.print(jsonObject);
			out.close();
		} catch (Exception ex) {
			logger.error("Loi", ex);
			throw ex;
		}
	}

	public static void jsonArrySerialize(HttpServletResponse response, Object obj) throws Exception {
		try {
			response.setContentType("application/json;charset=utf-8");
			response.setHeader("Cache-Control", "no-store");
			PrintWriter out = response.getWriter();
			JSONArray jsonArray = new JSONArray(new ObjectMapper().writeValueAsString(obj));
			out.print(jsonArray);
			out.close();
		} catch (Exception ex) {
			logger.error("Loi", ex);
			throw ex;
		}
	}

	public static void jsonObjectSerialize(HttpServletResponse response, JSONObject obj) throws Exception {
		try {
			response.setContentType("application/json;charset=utf-8");
			response.setHeader("Cache-Control", "no-store");
			PrintWriter out = response.getWriter();
			out.print(obj);
			out.close();
		} catch (Exception ex) {
			logger.error("Loi", ex);
			throw ex;
		}

	}

	public ObjectMapper nullMapper = new ObjectMapper().setSerializerProvider(new CustomSerializerProvider())
			.registerModule(new SimpleModule().addSerializer(Date.class, new JsonSerializer<Date>() {
				@Override
				public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
						throws IOException, JsonProcessingException {
					if (date == null) {
						jsonGenerator.writeNull();
					} else {
						DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
						// You can set timezone however you wish
						// df.setTimeZone(TimeZone.getDefault());
						jsonGenerator.writeObject(df.format(date));
					}
				}
			}));

	public ObjectMapper xx() {
		ObjectMapper nullMapper = new ObjectMapper().setSerializerProvider(new CustomSerializerProvider())
				.registerModule(new SimpleModule().addSerializer(Date.class, new JsonSerializer<Date>() {
					@Override
					public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
							throws IOException, JsonProcessingException {
						if (date == null) {
							jsonGenerator.writeNull();
						} else {
							DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
							// You can set timezone however you wish
							// df.setTimeZone(TimeZone.getDefault());
							jsonGenerator.writeObject(df.format(date));
						}
					}
				}));
		return nullMapper;
	}

}
