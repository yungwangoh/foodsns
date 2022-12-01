package sejong.foodsns.service.redis;

import java.time.Duration;

public interface RedisService {

    void setValues(String key, String value);
    void setValues(String key, String value, Duration duration);
    String getValues(String key);
    void deleteValues(String key);
}
