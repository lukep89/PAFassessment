package ibf2022.PAFassessment.model;

import java.io.StringReader;
import java.time.LocalDate;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Transfer {

    @NotNull(message = "You forgot to select a account to transfer from")
    private String fromAccount;

    @NotNull(message = "You forgot to select a account to transfer to")
    private String toAccount;

    @Min(value = 10, message = "Minimum transfer is $10")
    private Float amount = 0f;

    private String comments;

    private String transactionId;

    private LocalDate date;

    public static void create(JsonObject json) {

        Transfer transfer = new Transfer();
        transfer.setTransactionId(json.getString("transactionId"));
        transfer.setDate(LocalDate.parse(json.getString("date")));
        transfer.setFromAccount(json.getString("from_account"));
        transfer.setToAccount(json.getString("to_account"));
        transfer.setAmount(Float.parseFloat(json.getString("amount")));

    }

    public static JsonObject toJson(String jsonStr) {
        JsonReader reader = Json.createReader(new StringReader(jsonStr));
        return reader.readObject();

    }

    public JsonObject toJson() {
        // from Java Object to JSON Object
        return Json.createObjectBuilder()
                .add("transactionId", getTransactionId())
                .add("date", getDate().toString())
                .add("from_account", getFromAccount())
                .add("to_account", getToAccount())
                .add("amount", getAmount())
                .build();
    }

}
