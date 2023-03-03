package ibf2022.PAFassessment.service;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import ibf2022.PAFassessment.model.Transfer;

@Service
public class LogAuditService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void saveToRedis(Transfer transfer) {

        String transacId = UUID.randomUUID().toString().substring(0, 10);
        transfer.setTransactionId(transacId);
        transfer.setDate(LocalDate.now());

        System.out.println("======== @4 LogAudi transfer: " + transfer.toString() + "\n\n");

        redisTemplate.opsForValue().set(transfer.getTransactionId(), transfer.toJson().toString());

    }

}
