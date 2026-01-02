package MiniDidi.shared.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean; 
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * @author EddieLe@AutomationLine
 * @version 1.1
 * @since 2025/10/23 14:14
 */


@Service
public class PresetDataService implements InitializingBean {

    private List<PresetData> presetDataList;
    private final Random random = new Random();

    @Override
    public void afterPropertiesSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        File file = ResourceUtils.getFile("classpath:presets.json");
        this.presetDataList = mapper.readValue(file, new TypeReference<List<PresetData>>() {});
    }

    public PresetData getRandomPresetData() {
        if (presetDataList == null || presetDataList.isEmpty()) {
            throw new IllegalStateException("Preset data could not be loaded or is empty.");
        }
        return presetDataList.get(random.nextInt(presetDataList.size()));
    }
}
