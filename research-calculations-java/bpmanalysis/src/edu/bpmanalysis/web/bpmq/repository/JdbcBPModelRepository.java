package edu.bpmanalysis.web.bpmq.repository;

import edu.bpmanalysis.web.bpmq.entity.BPModel;
import edu.bpmanalysis.web.bpmq.entity.BinBPMQualityTuple;
import edu.bpmanalysis.web.bpmq.repository.api.BPModelRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcBPModelRepository implements BPModelRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int count() {
        return jdbcTemplate.queryForObject("select count(*) from bpmodels", Integer.class);
    }

    @Override
    public int countCorrectModels() {
        return jdbcTemplate.queryForObject("select count(*) from bpmodels where hardQuality = 1", Integer.class);
    }

    @Override
    public int countInvalidModels() {
        return jdbcTemplate.queryForObject("select count(*) from bpmodels where hardQuality < 1", Integer.class);
    }

    @Override
    public int countWithInvalidNodes() {
        return jdbcTemplate.queryForObject("select count(*) from bpmodels where sizeQualityH = 1", Integer.class);
    }

    @Override
    public int countWithInvalidDegrees() {
        return jdbcTemplate.queryForObject("select count(*) from bpmodels where degreesQualityH = 1", Integer.class);
    }

    @Override
    public int countWithInvalidEvents() {
        return jdbcTemplate.queryForObject("select count(*) from bpmodels where eventsQualityH = 1", Integer.class);
    }

    @Override
    public int countWithGatewaysMismatch() {
        return jdbcTemplate.queryForObject("select count(*) from bpmodels where gatewaysQualityH = 1", Integer.class);
    }

    @Override
    public int countWithOrGateways() {
        return jdbcTemplate.queryForObject("select count(*) from bpmodels where orQualityH = 1", Integer.class);
    }

    @Override
    public void save(BPModel bpModel) {
        jdbcTemplate.update(
                "insert into bpmodels (fileName, totalNodes, invalidNodes, startEvents, endEvents, unmatchedGateways, " +
                        "totalGateways, orGateways, sizeQualityH, degreesQualityH, eventsQualityH, gatewaysQualityH, " +
                        "orQualityH, sizeQualityS, degreesQualityS, eventsQualityS, gatewaysQualityS, orQualityS, " +
                        "hardQuality, softQuality) " +
                        "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                new Object[]{bpModel.getFileName(), bpModel.getTotalNodes(), bpModel.getInvalidNodes(), bpModel.getStartEvents(),
                        bpModel.getEndEvents(), bpModel.getUnmatchedGateways(), bpModel.getTotalGateways(),
                        bpModel.getOrGateways(), bpModel.getSizeQualityH(), bpModel.getDegreesQualityH(), bpModel.getEventsQualityH(),
                        bpModel.getGatewaysQualityH(), bpModel.getOrQualityH(), bpModel.getSizeQualityS(), bpModel.getDegreesQualityS(),
                        bpModel.getEventsQualityS(), bpModel.getGatewaysQualityS(), bpModel.getOrQualityS(),
                        bpModel.getHardQuality(), bpModel.getSoftQuality()});
    }

    @Override
    public void update(BPModel bpModel) {
        jdbcTemplate.update(
                "update bpmodels set fileName = ?, totalNodes = ?, invalidNodes = ?, startEvents = ?, endEvents = ?, " +
                        "unmatchedGateways = ?, totalGateways = ?, orGateways = ?, sizeQualityH = ?, degreesQualityH = ?, " +
                        "eventsQualityH = ?, gatewaysQualityH = ?, orQualityH = ?, sizeQualityS = ?, degreesQualityS = ?, " +
                        "eventsQualityS = ?, gatewaysQualityS = ?, orQualityS = ?, hardQuality = ?, softQuality = ?" +
                        "where fileName = ?",
                new Object[]{bpModel.getTotalNodes(), bpModel.getInvalidNodes(), bpModel.getStartEvents(),
                        bpModel.getEndEvents(), bpModel.getUnmatchedGateways(), bpModel.getTotalGateways(),
                        bpModel.getOrGateways(), bpModel.getSizeQualityH(), bpModel.getDegreesQualityH(), bpModel.getEventsQualityH(),
                        bpModel.getGatewaysQualityH(), bpModel.getOrQualityH(), bpModel.getSizeQualityS(), bpModel.getDegreesQualityS(),
                        bpModel.getEventsQualityS(), bpModel.getGatewaysQualityS(), bpModel.getOrQualityS(),
                        bpModel.getHardQuality(), bpModel.getSoftQuality(), bpModel.getFileName()});
    }

    @Override
    public void deleteByFileName(String fileName) {
        jdbcTemplate.update("delete from bpmodels where fileName = ?", new Object[]{fileName});
    }

    @Override
    public List<BPModel> findAll() {
        return jdbcTemplate.query(
                "select * from bpmodels",
                (rs, rowNum) ->
                        new BPModel(
                                rs.getString("fileName"),
                                rs.getInt("totalNodes"),
                                rs.getInt("invalidNodes"),
                                rs.getInt("startEvents"),
                                rs.getInt("endEvents"),
                                rs.getInt("unmatchedGateways"),
                                rs.getInt("totalGateways"),
                                rs.getInt("orGateways"),
                                rs.getInt("sizeQualityH"),
                                rs.getInt("degreesQualityH"),
                                rs.getInt("eventsQualityH"),
                                rs.getInt("gatewaysQualityH"),
                                rs.getInt("orQualityH"),
                                rs.getDouble("sizeQualityS"),
                                rs.getDouble("degreesQualityS"),
                                rs.getDouble("eventsQualityS"),
                                rs.getDouble("gatewaysQualityS"),
                                rs.getDouble("orQualityS"),
                                rs.getDouble("hardQuality"),
                                rs.getDouble("softQuality")
                        )
        );
    }

    @Override
    public List<BPModel> findByFileName(String fileName) {
        return jdbcTemplate.query(
                "select * from bpmodels where fileName = ?",
                new Object[]{fileName},
                (rs, rowNum) ->
                        new BPModel(
                                rs.getString("fileName"),
                                rs.getInt("totalNodes"),
                                rs.getInt("invalidNodes"),
                                rs.getInt("startEvents"),
                                rs.getInt("endEvents"),
                                rs.getInt("unmatchedGateways"),
                                rs.getInt("totalGateways"),
                                rs.getInt("orGateways"),
                                rs.getInt("sizeQualityH"),
                                rs.getInt("degreesQualityH"),
                                rs.getInt("eventsQualityH"),
                                rs.getInt("gatewaysQualityH"),
                                rs.getInt("orQualityH"),
                                rs.getDouble("sizeQualityS"),
                                rs.getDouble("degreesQualityS"),
                                rs.getDouble("eventsQualityS"),
                                rs.getDouble("gatewaysQualityS"),
                                rs.getDouble("orQualityS"),
                                rs.getDouble("hardQuality"),
                                rs.getDouble("softQuality")
                        )
        );
    }

    @Override
    public List<BPModel> findCorrectModels() {
        return jdbcTemplate.query(
                "select * from bpmodels where hardQuality = ?",
                new Object[]{1},
                (rs, rowNum) ->
                        new BPModel(
                                rs.getString("fileName"),
                                rs.getInt("totalNodes"),
                                rs.getInt("invalidNodes"),
                                rs.getInt("startEvents"),
                                rs.getInt("endEvents"),
                                rs.getInt("unmatchedGateways"),
                                rs.getInt("totalGateways"),
                                rs.getInt("orGateways"),
                                rs.getInt("sizeQualityH"),
                                rs.getInt("degreesQualityH"),
                                rs.getInt("eventsQualityH"),
                                rs.getInt("gatewaysQualityH"),
                                rs.getInt("orQualityH"),
                                rs.getDouble("sizeQualityS"),
                                rs.getDouble("degreesQualityS"),
                                rs.getDouble("eventsQualityS"),
                                rs.getDouble("gatewaysQualityS"),
                                rs.getDouble("orQualityS"),
                                rs.getDouble("hardQuality"),
                                rs.getDouble("softQuality")
                        )
        );
    }

    @Override
    public List<BPModel> findInvalidModels() {
        return jdbcTemplate.query(
                "select * from bpmodels where hardQuality < ?",
                new Object[]{1},
                (rs, rowNum) ->
                        new BPModel(
                                rs.getString("fileName"),
                                rs.getInt("totalNodes"),
                                rs.getInt("invalidNodes"),
                                rs.getInt("startEvents"),
                                rs.getInt("endEvents"),
                                rs.getInt("unmatchedGateways"),
                                rs.getInt("totalGateways"),
                                rs.getInt("orGateways"),
                                rs.getInt("sizeQualityH"),
                                rs.getInt("degreesQualityH"),
                                rs.getInt("eventsQualityH"),
                                rs.getInt("gatewaysQualityH"),
                                rs.getInt("orQualityH"),
                                rs.getDouble("sizeQualityS"),
                                rs.getDouble("degreesQualityS"),
                                rs.getDouble("eventsQualityS"),
                                rs.getDouble("gatewaysQualityS"),
                                rs.getDouble("orQualityS"),
                                rs.getDouble("hardQuality"),
                                rs.getDouble("softQuality")
                        )
        );
    }

    @Override
    public List<BPModel> findModelsByBinaryQualityTuple(BinBPMQualityTuple qualityTuple) {
        return jdbcTemplate.query(
                "select * from bpmodels where sizeQualityH = ? and degreesQualityH = ? and eventsQualityH = ?" +
                        " and gatewaysQualityH = ? and orQualityH = ?",
                new Object[]{qualityTuple.getSizeQualityH(), qualityTuple.getDegreesQualityH(),
                        qualityTuple.getEventsQualityH(), qualityTuple.getGatewaysQualityH(), qualityTuple.getOrQualityH()},
                (rs, rowNum) ->
                        new BPModel(
                                rs.getString("fileName"),
                                rs.getInt("totalNodes"),
                                rs.getInt("invalidNodes"),
                                rs.getInt("startEvents"),
                                rs.getInt("endEvents"),
                                rs.getInt("unmatchedGateways"),
                                rs.getInt("totalGateways"),
                                rs.getInt("orGateways"),
                                rs.getInt("sizeQualityH"),
                                rs.getInt("degreesQualityH"),
                                rs.getInt("eventsQualityH"),
                                rs.getInt("gatewaysQualityH"),
                                rs.getInt("orQualityH"),
                                rs.getDouble("sizeQualityS"),
                                rs.getDouble("degreesQualityS"),
                                rs.getDouble("eventsQualityS"),
                                rs.getDouble("gatewaysQualityS"),
                                rs.getDouble("orQualityS"),
                                rs.getDouble("hardQuality"),
                                rs.getDouble("softQuality")
                        )
        );
    }
}
