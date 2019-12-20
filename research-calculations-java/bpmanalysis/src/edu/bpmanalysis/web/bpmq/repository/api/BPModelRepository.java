package edu.bpmanalysis.web.bpmq.repository.api;

import edu.bpmanalysis.web.bpmq.entity.BPModel;
import edu.bpmanalysis.web.bpmq.entity.BinBPMQualityTuple;

import java.util.List;

public interface BPModelRepository {

    int count();

    int countCorrectModels();

    int countInvalidModels();

    int countWithInvalidNodes();

    int countWithInvalidDegrees();

    int countWithInvalidEvents();

    int countWithGatewaysMismatch();

    int countWithOrGateways();

    void save(BPModel bpModel);

    void update(BPModel bpModel);

    void deleteByFileName(String fileName);

    List<BPModel> findAll();

    List<BPModel> findByFileName(String fileName);

    List<BPModel> findCorrectModels();

    List<BPModel> findInvalidModels();

    List<BPModel> findModelsByBinaryQualityTuple(BinBPMQualityTuple qualityTuple);
}
