package ibf2022.PAFassessment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Transfer {
    
    private String fromAccount;
    private String toAccount;
    private String amount;
    private String comments;




}
