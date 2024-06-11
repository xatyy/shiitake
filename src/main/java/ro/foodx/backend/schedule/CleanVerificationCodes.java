package ro.foodx.backend.schedule;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ro.foodx.backend.model.user.Verification;
import ro.foodx.backend.repository.VerificationRepository;
import java.util.List;

@Slf4j
@EnableAsync
@Getter
@Setter
@RequiredArgsConstructor
@Component
public class CleanVerificationCodes {

    private VerificationRepository verificationRepository;

    @Autowired
    public CleanVerificationCodes(VerificationRepository verificationRepository) {
        this.verificationRepository = verificationRepository;
    }

    @Async
    @Scheduled(fixedRate = 10 * 60 * 1000)
    public void cleanVerificationCodes() throws InterruptedException {
        Long currentTimestampMillis = System.currentTimeMillis();

        Long newTimestampMillis = currentTimestampMillis - (10 * 60 * 1000);

        List<Verification> expiredCodes = verificationRepository.findByTimestampLessThan(newTimestampMillis);

        if(!expiredCodes.isEmpty()){
            verificationRepository.deleteAllInBatch(expiredCodes);
            log.info("Deleted in batch");
        }
        Thread.sleep(2000);
    }
}
