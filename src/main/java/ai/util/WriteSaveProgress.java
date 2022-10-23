package ai.util;

import ai.service.FileController;
import ai.service.NeiroOld;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WriteSaveProgress {

    Logger logger = LoggerFactory.getLogger(WriteSaveProgress.class);

    private final NeiroOld neiroOld;

    private final FileController fileController;

    public void save() {
        fileController.saveNeiro(neiroOld);
        neiroOld.print();
        logger.info("saved");
    }

    public void load() {
        try {
            neiroOld.setPerceptronOlds(fileController.loadNeiro().getPerceptronOlds());
        } catch (Exception e) {
            logger.warn(e.getMessage());
            neiroOld.postConstruct();
        }
        neiroOld.print();
        logger.info("loaded");
    }

}
