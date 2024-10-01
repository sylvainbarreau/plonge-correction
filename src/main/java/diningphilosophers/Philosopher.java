package diningphilosophers;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Philosopher extends Thread {
    private final static int delai = 1000;
    private final ChopStick myLeftStick;
    private final ChopStick myRightStick;
    private boolean running = true;

    public Philosopher(String name, ChopStick left, ChopStick right) {
        super(name);
        myLeftStick = left;
        myRightStick = right;
    }

    private void think() throws InterruptedException {
        System.out.println("M."+this.getName()+" pense... ");
        sleep(delai+new Random().nextInt(delai+1));
        System.out.println("M."+this.getName()+" arrête de penser");
    }

    private void eat() throws InterruptedException {
        System.out.println("M."+this.getName() + " MANGE...");
        sleep(delai+new Random().nextInt(delai+1));
        //System.out.println("M."+this.getName()+" arrête de manger");
    }

    @Override
    public void run() {
        while (running) {
            try {
                think();
                // Aléatoirement prendre la baguette de gauche puis de droite ou l'inverse
                boolean gauche=false;
                boolean droite=false;
                switch(new Random().nextInt(2)) {
                    case 0:
                        gauche = myLeftStick.take();
                        think(); // pour augmenter la probabilité d'interblocage
                        droite = myRightStick.take();
                        break;
                    case 1:
                        droite = myRightStick.take();
                        think(); // pour augmenter la probabilité d'interblocage
                        gauche = myLeftStick.take();
                }
                if (gauche && droite) { // Si on arrive ici, on a pu "take" les 2 baguettes
                eat();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger("Table").log(Level.SEVERE, "{0} Interrupted", this.getName());
            } finally {
                // On libère les baguettes :
                myLeftStick.release();
                myRightStick.release();
            } 
        }
    }

    // Permet d'interrompre le philosophe "proprement" :
    // Il relachera ses baguettes avant de s'arrêter
    public void leaveTable() {
        running = false;
    }

}
