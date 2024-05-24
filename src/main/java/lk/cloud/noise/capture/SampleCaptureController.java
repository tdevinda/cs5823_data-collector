package lk.cloud.noise.capture;

import lk.cloud.noise.dto.NoiseSample;
import lk.cloud.noise.dto.SampleResponse;
import lk.cloud.noise.repository.SampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SampleCaptureController {

    @Autowired
    private SampleRepository sampleRepository;

    @PostMapping(path = "/sample")
    public SampleResponse captureSampleInformation(@RequestBody NoiseSample sample) {
        sampleRepository.saveSample(sample);
        return new SampleResponse();
    }

    /**
     * Returns all the NoiseSample items of the redis database as a list when requested.
     */
    @GetMapping("/flush")
    @ResponseBody
    public List<NoiseSample> getAllSamples() {
        List<NoiseSample> noiseSampleList = new ArrayList<>();

        List<Integer> sampleIds = sampleRepository.getAllSavedIds();
        sampleIds.forEach(sampleId -> {
            noiseSampleList.addAll(sampleRepository.getAllSamplesForId(sampleId));
        });

        return noiseSampleList;
    }



}
