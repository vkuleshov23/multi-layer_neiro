package ai.util;

import ai.service.FileController;
import ai.service.Neiro;
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

    private final Neiro neiro;

    private final FileController fileController;

    public void save() {
        fileController.saveNeiroOld(neiroOld);
        neiroOld.print();
        fileController.saveNeiro(neiro);
        logger.info("saved");
    }

    public void load() {
        try {
            neiroOld.setPerceptronOlds(fileController.loadNeiroOld().getPerceptronOlds());
        } catch (Exception e) {
            logger.warn(e.getMessage());
            neiroOld.postConstruct();
            neiroOld.print();
        }
        try {
            neiro.setSymbolNeuralNet(fileController.loadNeiro().getSymbolNeuralNet());
            logger.info(neiro.getSymbolNeuralNet().toString());
        } catch (Exception e) {
            neiro.postConstruct();
            logger.error(e.getMessage());
        }
        logger.info("loaded");
    }

}
