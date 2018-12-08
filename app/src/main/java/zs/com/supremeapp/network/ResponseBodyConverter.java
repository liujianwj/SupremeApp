package zs.com.supremeapp.network;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;

import java.io.IOException;
import java.io.StringReader;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import zs.com.supremeapp.exception.ApiException;
import zs.com.supremeapp.model.Result;

/**
 * Created by liujian on 2018/9/4.
 */

public class ResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private static final int SUCCESS = 200;       // 成功

    private final Gson gson;
    private final TypeAdapter<T> adapter;

    ResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String json = value.string();
        try {
            verify(json);
            JsonObject jsonObject = (JsonObject) new JsonParser().parse(json);
            JsonElement jsonElement = jsonObject.get("data");
            if(jsonElement.isJsonArray()){
                String a = "{'data':{}}";
                return adapter.read(gson.newJsonReader(new StringReader(((JsonObject) new JsonParser().parse(a)).get("data").getAsJsonObject().toString())));
            }else {
                return adapter.read(gson.newJsonReader(new StringReader(jsonObject.get("data").getAsJsonObject().toString())));
            }
        } finally {
            value.close();
        }
    }

    private void verify(String json) {
        Result result = gson.fromJson(json, Result.class);
        if (result.getFlag() != SUCCESS) {
//            switch (result.getFlag()) {
//                case FAILURE:
//                case SERVER_EXCEPTION:
//                    throw new ApiException(result.getMsg());
//                case TOKEN_EXPIRE:
//                    throw new TokenExpireException(result.msg);
//                default:
//                    throw new UndefinedStateException();
//            }
            throw new ApiException(result.getMsg(), new Throwable(result.getFlag() + ""));
        }
    }
}
