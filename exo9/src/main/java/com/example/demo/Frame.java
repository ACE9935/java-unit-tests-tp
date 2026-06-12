import java.util.ArrayList;
import java.util.List;

public class Frame {
    private int score = 0;
    private boolean lastFrame;
    private IGenerateur generateur;
    private List<Roll> rolls = new ArrayList<>();
    
    public Frame(IGenerateur generateur, boolean lastFrame) {
        this.lastFrame = lastFrame;
        this.generateur = generateur;
    }
    
    public boolean makeRoll() {
        int currentRollCount = rolls.size();
        if (!lastFrame) {
            if (currentRollCount >= 2) {
                return false;
            }
            if (currentRollCount == 1 && rolls.get(0).getPins() == 10) {
                return false;
            }
            int maxPins = (currentRollCount == 0) ? 10 : (10 - rolls.get(0).getPins());
            int pinsDown = generateur.randomPin(maxPins);
            
            rolls.add(new Roll(pinsDown));
            score += pinsDown;
            return true;
        }

        else {
            if (currentRollCount >= 3) {
                return false;
            }
            if (currentRollCount == 2) {
                boolean firstWasStrike = rolls.get(0).getPins() == 10;
                boolean isSpare = (rolls.get(0).getPins() + rolls.get(1).getPins() == 10);
                
                // On n'autorise le 3ème lancer que s'il y a eu Strike au début ou un Spare
                if (!firstWasStrike && !isSpare) {
                    return false;
                }
            }
            int maxPins = 10;
            if (currentRollCount == 1) {
                if (rolls.get(0).getPins() < 10) {
                    maxPins = 10 - rolls.get(0).getPins();
                }
            } else if (currentRollCount == 2) {
                if (rolls.get(0).getPins() == 10) {
                    if (rolls.get(1).getPins() < 10) {
                        maxPins = 10 - rolls.get(1).getPins();
                    }
                }
            }

            int pinsDown = generateur.randomPin(maxPins);
            rolls.add(new Roll(pinsDown));
            score += pinsDown;
            return true;
        }
    }

    public int getScore() {
        return this.score;
    }
}