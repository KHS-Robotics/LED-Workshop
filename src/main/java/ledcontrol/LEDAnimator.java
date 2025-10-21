package ledcontrol;

import java.awt.Color;

/** Controls LED animations using predefined color patterns and sequences. */
public class LEDAnimator {

    private LEDController controller;
    private int numLEDs;

    /** Initializes the animator with a given number of LEDs. */
    public LEDAnimator(int numLEDs) {
        this.controller = new LEDController();
        this.numLEDs = numLEDs;
    }

    /** Turns on LED 0 to a solid red color. */
    public void allRed() {
        for (int i = 0; i < this.numLEDs; i++) {
            this.controller.sendRGB(i, 128, 0, 0);
        }
    }

    /** Displays a static rainbow pattern. */
    public void rainbow() {
        this.controller.sendRGB(0, 255, 0, 0);
        this.controller.sendRGB(1, 255, 128, 0);
        this.controller.sendRGB(2, 255, 255, 0);
        this.controller.sendRGB(3, 0, 255, 0);
        this.controller.sendRGB(4, 0, 255, 255);
        this.controller.sendRGB(5, 0, 128, 255);
        this.controller.sendRGB(6, 0, 0, 255);
        this.controller.sendRGB(7, 64, 0, 128);
    }

    /** Alternates red and blue colors across the LED strip. */
    public void alternateRedBlue() {
        for (int i = 0; i < this.numLEDs; i++) {
            if (i % 2 == 0) {
                this.controller.sendRGB(i, 128, 0, 0);
            } else {
                this.controller.sendRGB(i, 0, 0, 128);
            }
        }
    }

    /** Creates a red-to-green gradient across the LED strip. */
    public void gradientRedGreen() {
        for (int i = 0; i < this.numLEDs; i++) {
            int green = (int) (i / ((double) numLEDs - 1) * 255);
            int red = (int) ((numLEDs - 1 - i) / ((double) numLEDs - 1) * 255);
            this.controller.sendRGB(i, red, green, 0);
        }
    }

    /** Turns lights on one at a time, then off one at a time. */
    public void growAndShrink() throws InterruptedException {
        for (int i = 0; i < this.numLEDs; i++) {
            this.controller.sendRGB(i, 0, 64, 0);
            Thread.sleep(50);
        }
        for (int i = 0; i < this.numLEDs; i++) {
            this.controller.sendRGB(i, 0, 0, 0);
            Thread.sleep(50);
        }
    }

    /** Blink the LEDs at increasing frequency. */
    public void blinkSequence() throws InterruptedException {
        for (int i = 0; i < this.numLEDs; i++) {
            for (int k = 0; k <= i; k++) {
                this.controller.sendRGB(i, 64, 64, 64);
                Thread.sleep(1000 / (i + 1));
                this.controller.sendRGB(i, 0, 0, 0);
                Thread.sleep(1000 / (i + 1));
            }
        }
    }

    /** Animates a ping-pong motion: yellow forward, cyan backward. */
    public void pingPong() throws InterruptedException {
        this.controller.sendRGB(0, 128, 128, 0);
        this.controller.sendRGB(this.numLEDs - 1, 0, 128, 128);

        for (int i = 1; i < this.numLEDs - 1; i++) {
            this.controller.sendRGB(i, 128, 128, 0);
            Thread.sleep(150);
            this.controller.sendRGB(i, 0, 0, 0);
        }

        for (int i = this.numLEDs - 2; i > 0; i--) {
            this.controller.sendRGB(i, 0, 128, 128);
            Thread.sleep(150);
            this.controller.sendRGB(i, 0, 0, 0);
        }
    }

    /** Cycles red, blue, and white down the strip. */
    public void barberPole() throws InterruptedException {
        for (int t = 0; t < 100; t++) {
            for (int i = 0; i < this.numLEDs; i++) {
                int counter = (t + i) % 9;
                if (counter < 3) {
                    this.controller.sendRGB(i, 128, 0, 0);
                }
                else if (counter >= 3 && counter < 6) {
                    this.controller.sendRGB(i, 0, 0, 128);
                }
                else {
                    this.controller.sendRGB(i, 128, 128, 128);
                }
            }
            Thread.sleep(200);
        }
    }

    /** Animates a seesaw motion: orange sweep forward, green sweep backward. */
    public void seesawOrangeGreen() throws InterruptedException {
        for (int i = 0; i < this.numLEDs; i++) {
            this.controller.sendRGB(i, 128, 64, 0);
            Thread.sleep(100);
        }
        for (int i = 0; i < this.numLEDs; i++) {
            this.controller.sendRGB(this.numLEDs - i - 1, 0, 128, 0);
            Thread.sleep(100);
        }
    }

    /** Creates a moving rainbow wipe with brightness falloff. */
    public void wipeRainbow() throws InterruptedException {
        Color[] rainbow = {
            Color.RED, Color.YELLOW, Color.GREEN,
            Color.CYAN, Color.BLUE, Color.MAGENTA
        };
        for (int t = 0; t < rainbow.length * this.numLEDs; t++) {
            for (int i = 0; i < this.numLEDs; i++) {
                int colorIndex = (t + i) / this.numLEDs % rainbow.length;
                Color color = rainbow[colorIndex];
                double brightness = (this.numLEDs - (t + i) % this.numLEDs) / ((double) this.numLEDs);
                brightness = Math.pow(brightness, 2);
                int red = (int) (brightness * color.getRed());
                int green = (int) (brightness * color.getGreen());
                int blue = (int) (brightness * color.getBlue());
                this.controller.sendRGB(i, red, green, blue);
            }
            Thread.sleep(45);
        }
    }

    /** Turns off all LEDs by setting them to black. */
    public void clear() {
        for (int i = 0; i < numLEDs; i++) {
            this.controller.sendRGB(i, 0, 0, 0);
        }
    }

    /** Runs a continuous demo loop cycling through animations. */
    public static void main(String[] args) throws InterruptedException {
        LEDAnimator animator = new LEDAnimator(8);

        animator.controller.sendRGB(0, 128, 0, 0);        

        while (true) {   
            Thread.sleep(3000);

            animator.allRed();
            Thread.sleep(3000);

            animator.rainbow();
            Thread.sleep(3000);

            animator.alternateRedBlue();
            Thread.sleep(3000);

            animator.gradientRedGreen();
            Thread.sleep(3000);

            animator.clear();
            for (int i = 0; i < 6; i++) {
                animator.controller.sendRGB(0, 128, 0, 0);
                Thread.sleep(500);
                animator.controller.sendRGB(0, 0, 0, 0);
                Thread.sleep(500);
            }

            for (int i = 0; i < 1; i++) {
                animator.blinkSequence();
            }

            for (int i = 0; i < 4; i++) {
                animator.growAndShrink();
            }

            for (int i = 0; i < 4; i++) {
                animator.pingPong();
            }

            for (int i = 0; i < 1; i++) {
                animator.barberPole();
            }

            for (int i = 0; i < 4; i++) {
                animator.seesawOrangeGreen();
            }
            
            for (int i = 0; i < 4; i++) {
                animator.wipeRainbow();
            }

            animator.clear();
        }
    }
}
