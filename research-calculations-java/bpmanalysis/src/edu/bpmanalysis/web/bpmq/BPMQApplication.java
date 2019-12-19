package edu.bpmanalysis.web.bpmq;

import edu.bpmanalysis.web.bpmq.entity.BPModel;
import edu.bpmanalysis.web.bpmq.repository.api.BPModelRepository;
import edu.bpmanalysis.web.bpmq.util.BPMGraphUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@SpringBootApplication
public class BPMQApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(BPMQApplication.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("jdbcBPModelRepository")
    private BPModelRepository bpModelRepository;

    public static void main(String[] args) {
        SpringApplication.run(BPMQApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        log.info("Setting up repository ...");

        jdbcTemplate.execute("drop table bpmodels if exists");
        jdbcTemplate.execute("create table bpmodels ( " +
                "fileName VARCHAR(255)," +
                "totalNodes INTEGER," +
                "invalidNodes INTEGER," +
                "startEvents INTEGER," +
                "endEvents INTEGER," +
                "unmatchedGateways INTEGER," +
                "totalGateways INTEGER," +
                "orGateways INTEGER," +
                "sizeQualityH INTEGER," +
                "degreesQualityH INTEGER," +
                "eventsQualityH INTEGER," +
                "gatewaysQualityH INTEGER," +
                "orQualityH INTEGER," +
                "sizeQualityS NUMERIC(5,2)," +
                "degreesQualityS NUMERIC(5,2)," +
                "eventsQualityS NUMERIC(5,2)," +
                "gatewaysQualityS NUMERIC(5,2)," +
                "orQualityS NUMERIC(5,2)," +
                "hardQuality NUMERIC(5,2)," +
                "softQuality NUMERIC(5,2) )");

        List<BPModel> bpModels = BPMGraphUtil.getReadyBPMNModels();

        bpModels.forEach(bpModel -> {
            log.info("Loading model ... {}", bpModel.getFileName());
            bpModelRepository.save(bpModel);
        });

        log.info("Total models: {}", bpModelRepository.count());
    }
}
