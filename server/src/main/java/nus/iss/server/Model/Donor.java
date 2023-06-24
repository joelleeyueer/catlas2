package nus.iss.server.Model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Donor {
    private String username;
    private double amount;
    private LocalDateTime donationDate;
}
