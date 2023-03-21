package code.troopers;

import code.troopers.service.BowlingService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {

        BowlingService bowlingService = new BowlingService();
        bowlingService.playParty();
    }
}