package code.troopers.service;

import code.troopers.entity.Frame;

import java.util.*;

public class BowlingService {

    static Scanner in = new Scanner(System.in);
    Frame frameBonus;
    List<Frame> frameList = new ArrayList<>();

    public void playParty() {
        this.frameList = this.init();

        String frameString = "Frame       |  1|  2|  3|  4|  5|  6|  7|  8|  9|  10|";

        this.printScoreBoard(frameString);

        for (int i = 0; i <= 9; i++) {

            Frame frame = this.frameList.get(i);

            this.firstRoll(frame);
            if (!isStrike(frame)) {
                this.secondRoll(frame);
            }

            this.calculScoreFrames(i);

            this.printScoreBoard(frameString);

            if (i == 9) {

                String frameBonusString = "Frame       |  1|  2|  3|  4|  5|  6|  7|  8|  9|  10|  11|";

                if (this.isStrike(frame)) {
                    this.playBonus(true);

                    this.printScoreBoard(frameBonusString);

                } else if (this.isSpare(frame)) {
                    this.playBonus(false);

                    this.printScoreBoard(frameBonusString);
                }
            }
        }

    }

    public List<Frame> init() {
        List<Frame> frames = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            frames.add(new Frame(i));
        }
        return frames;
    }

    private void printScoreBoard(String frameString) {
        System.out.println(frameString);
        this.printFrameScore();
        this.printPartyScore();
    }

    private void calculScoreFrames(Integer currentIndex) {

        Frame currentFrame = this.frameList.get(currentIndex);

        Frame previousFrame = null;
        Frame previousPreviousFrame = null;

        if (currentIndex >= 1) {
            previousFrame = this.frameList.get(currentIndex - 1);
        }

        if (currentIndex >= 2) {
            previousPreviousFrame = this.frameList.get(currentIndex - 2);
        }

        // Calcul de la frame courante si pas un strike ou un spare
        if (currentFrame != null && !isStrike(currentFrame) && !isSpare(currentFrame)) {
            if (currentFrame.getFirstRoll() != null && currentFrame.getSecondRoll() != null) {
                currentFrame.setFrameScore(currentFrame.getFirstRoll() + currentFrame.getSecondRoll());
                currentFrame.setCalculated(true);
            }
        }

        // Calcul de la frame - 1 si pas calculé au tour dernier
        if (previousFrame != null && !previousFrame.isCalculated()) {
            if (isStrike(previousFrame) && currentFrame.getSecondRoll() != null) {
                previousFrame.setFrameScore(previousFrame.getFirstRoll() + currentFrame.getFirstRoll() + currentFrame.getSecondRoll());
                previousFrame.setCalculated(true);
            } else if (isSpare(previousFrame)) {
                previousFrame.setFrameScore(previousFrame.getFirstRoll() + previousFrame.getSecondRoll() + currentFrame.getFirstRoll());
                previousFrame.setCalculated(true);
            }
        }

        // Calcul de la frame - 2 si pas calculé il y a deux tours
        if (previousPreviousFrame != null && !previousPreviousFrame.isCalculated()) {
            if (isStrike(previousPreviousFrame) && previousFrame.getSecondRoll() != null) {
                previousPreviousFrame.setFrameScore(previousPreviousFrame.getFirstRoll() + previousFrame.getFirstRoll() + previousFrame.getSecondRoll());
            } else {
                previousPreviousFrame.setFrameScore(previousPreviousFrame.getFirstRoll() + previousFrame.getFirstRoll() + currentFrame.getFirstRoll());
            }
            previousPreviousFrame.setCalculated(true);
        }
    }

    private Integer readInt() {
        while (!in.hasNextInt()) {
            System.out.println("Vous devez saisir un chiffre !");
            in.nextLine();
        }
        return in.nextInt();
    }

    private void firstRoll(Frame frame) {
        System.out.printf("-- Frame N°%d : 1er tir --%n", frame.getFrameNumber());
        int firstRoll = this.readInt();
        while (firstRoll < 0 || firstRoll > 10) {
            System.out.println("Vous devez saisir un chiffre entre 0 et 10 !");
            firstRoll = this.readInt();
        }
        frame.setFirstRoll(firstRoll);
    }

    private void secondRoll(Frame frame) {
        System.out.printf("-- Frame N°%d : 2ème tir --%n", frame.getFrameNumber());
        int secondRoll = this.readInt();

        while (secondRoll + frame.getFirstRoll() > (this.frameBonus == null ? 10 : 20)) {
            System.out.printf("Vous devez saisir un chiffre entre 0 et %s !%n", 10 - frame.getFirstRoll());
            secondRoll = this.readInt();
        }
        frame.setSecondRoll(secondRoll);
    }


    private void playBonus(boolean isStrike) {
        this.frameBonus = new Frame();
        this.frameBonus.setFrameNumber(11);
        firstRoll(frameBonus);
        if (isStrike) {
            secondRoll(frameBonus);
        }
    }

    private void printFrameScore() {
        StringBuilder frameScore = new StringBuilder("Frame score |");
        for (Frame frame : this.frameList) {
            printFrame(frameScore, frame);
        }

        if (this.frameBonus != null) {
            printFrameBonus(frameScore);
        }

        System.out.println(frameScore);
    }

    private void printFrame(StringBuilder frameScore, Frame frame) {
        if (isStrike(frame)) {
            frameScore.append(this.isFrameTenOrMore(frame)).append("  X|");
        } else if (isSpare(frame)) {
            frameScore.append(String.format("%d ", frame.getFirstRoll())).append(this.isFrameTenOrMore(frame)).append("/|");
        } else if (frame.getFirstRoll() != null && frame.getSecondRoll() != null) {
            frameScore.append(String.format(String.format("%d ", frame.getFirstRoll()))).append(this.isFrameTenOrMore(frame)).append((String.format("%d|", frame.getFirstRoll())));
        } else {
            frameScore.append(this.isFrameTenOrMore(frame)).append("   |");
        }
    }

    private void printFrameBonus(StringBuilder frameScore) {
        if (this.frameBonus.getFirstRoll() == 10 && this.frameBonus.getSecondRoll() == null) {
            frameScore.append("X   |");
        } else if (this.frameBonus.getSecondRoll() == null) {
            frameScore.append(String.format("%d  |", this.frameBonus.getFirstRoll()));
        } else if (this.frameBonus.getFirstRoll() == 10 && this.frameBonus.getSecondRoll() == 10) {
            frameScore.append("X  X|");
        } else if (this.frameBonus.getFirstRoll() == 10 && this.frameBonus.getSecondRoll() != null) {
            frameScore.append(String.format("X  %d|", this.frameBonus.getSecondRoll()));
        } else if (this.frameBonus.getFirstRoll() != null && this.frameBonus.getSecondRoll() == 10) {
            frameScore.append(String.format("%d  X|", this.frameBonus.getSecondRoll()));
        } else if (this.frameBonus.getFirstRoll() + this.frameBonus.getSecondRoll() == 10) {
            frameScore.append(String.format("%d  /|", this.frameBonus.getSecondRoll()));
        } else {
            frameScore.append(String.format("%d  %d|", this.frameBonus.getFirstRoll(), this.frameBonus.getSecondRoll()));
        }
    }

    private void printPartyScore() {
        StringBuilder partyScore = new StringBuilder("Score       |");
        int score = 0;
        for (Frame frame : this.frameList) {
            if (frame.getFrameScore() != null) {
                score += frame.getFrameScore();
                if (score > 99) {
                    partyScore.append(this.isFrameTenOrMore(frame)).append(String.format("%s|", score));
                } else if (score > 9) {
                    partyScore.append(this.isFrameTenOrMore(frame)).append(String.format(" %s|", score));
                } else {
                    partyScore.append(this.isFrameTenOrMore(frame)).append(String.format("  %s|", score));
                }
            } else {
                if (this.frameBonus != null) {
                    if (isStrike(frame)) {
                        score += this.frameList.get(9).getFirstRoll();
                    } else {
                        score += this.frameList.get(9).getFirstRoll() + this.frameList.get(9).getSecondRoll();
                    }
                    if (this.frameBonus.getSecondRoll() != null) {
                        score += this.frameBonus.getFirstRoll() + this.frameBonus.getSecondRoll();
                    } else {
                        score += this.frameList.get(9).getFirstRoll();
                        score += this.frameBonus.getFirstRoll();
                    }
                    if (score > 99) {
                        partyScore.append(this.isFrameTenOrMore(frame)).append(String.format("%s|", score));
                    } else if (score > 9) {
                        partyScore.append(this.isFrameTenOrMore(frame)).append(String.format(" %s|", score));
                    } else {
                        partyScore.append(this.isFrameTenOrMore(frame)).append(String.format("  %s|", score));
                    }
                } else {
                    partyScore.append(this.isFrameTenOrMore(frame)).append("   |");
                }
            }
        }
        System.out.println(partyScore);
    }

    private boolean isStrike(Frame frame) {
        if (frame.getFirstRoll() != null) {
            return frame.getFirstRoll() == 10;
        } else {
            return false;
        }
    }

    private boolean isSpare(Frame frame) {
        if (frame.getFirstRoll() != null && frame.getSecondRoll() != null) {
            return frame.getFirstRoll() + frame.getSecondRoll() == 10;
        } else {
            return false;
        }
    }

    private String isFrameTenOrMore(Frame frame) {
        if (frame.getFrameNumber() > 9) {
            return " ";
        } else {
            return "";
        }
    }
}
