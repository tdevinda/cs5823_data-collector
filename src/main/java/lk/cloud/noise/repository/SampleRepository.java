package lk.cloud.noise.repository;

import lk.cloud.noise.dto.NoiseSample;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class SampleRepository {

    private Logger logger = LogManager.getLogger(getClass());

    @Autowired
    RedisTemplate<String, Float> redisTemplate;

    public void saveSample(NoiseSample sample) {
        logger.info("Saving sample {}", sample);
        redisTemplate.opsForList().leftPush("id:" + sample.getId(), sample.getLevel());
    }

    public List<Integer> getAllSavedIds() {
        Set<String> keySet = redisTemplate.keys("id:*");
        logger.info("Found keys {}", keySet);

        return keySet.stream().map(k -> Integer.parseInt(k.substring(3))).collect(Collectors.toList());
    }

    public List<NoiseSample> getAllSamplesForId(int id) {
        List<NoiseSample> sampleList = new ArrayList<>();
        Float value = null;
        while ((value = redisTemplate.opsForList().rightPop("id:" + id)) != null) {
            sampleList.add(new NoiseSample(
                    id,
                    value
            ));
        }
        return sampleList;
    }
}
