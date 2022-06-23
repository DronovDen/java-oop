package model;

import controller.ThreadCollision;
import controller.ThreadFood;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public class Game implements Serializable {

    public static final int FOOD_COUNT = 100;
    public final static Long TIME_START = (long) 600000;
    public final static Long TIME_OUT = (long) 60000;
    public final static int MIN_PLAYERS = 2;
    public final static int MAX_PLAYERS = 2;
    private static final long serialVersionUID = 1L;
    private ArrayList<Circle> food;
    private ArrayList<Circle> circles;
    private boolean isRunning;
    private boolean timeout;

    public Game() {
        this.food = new ArrayList<Circle>();
        this.circles = new ArrayList<Circle>();
        isRunning = false;
        timeout = false;
        initializeFood();
    }

    public void startGame() {
        isRunning = true;
        timeout = false;

        ThreadFood f = new ThreadFood(this);
        f.start();

        ThreadCollision c = new ThreadCollision(this);
        c.start();
    }

    public void initializeFood() {
        for (int i = 0; i < FOOD_COUNT; i++) {
            Circle a = new Circle();
            food.add(a);
        }
    }

    public void deleteFood() {
        ArrayList<Integer> aux = new ArrayList<Integer>();
        for (int i = 0; i < food.size(); i++) {
            if (food.get(i) != null) {
                if (!food.get(i).isAlive()) {
                    aux.add(i);
                }
            }
        }
        for (Integer integer : aux) {
            this.food.remove((int) integer);
        }
    }

    public void deleteAvatars() {
        ArrayList<Integer> aux = new ArrayList<Integer>();
        for (int i = 0; i < circles.size(); i++) {
            if (!circles.get(i).isAlive()) {
                aux.add(i);
            }
        }

        for (Integer integer : aux) {
            circles.remove((int) integer);
        }
    }

    public Circle getAvatar(int id) {
        for (Circle circle : circles) {
            if (circle.getId() == id) {
                return circle;
            }
        }
        return null;
    }

    public void addAvatar(String nickName, int id) {
        Circle a = new Circle(nickName, id);
        circles.add(a);
    }

    public int getIdAvailable() {
        return circles.size();

    }

    public void move(double x, double y, int id) {
        Circle circle = getAvatar(id);
        if (circle != null) {
            circle.move(x, y);
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean isOn) {
        this.isRunning = isOn;
    }

    public ArrayList<Circle> getFood() {
        return food;
    }

    public ArrayList<Circle> getAvatars() {
        return circles;
    }

    /*public void render(Graphics g, double scale) {
        for (int i = 0; i < avatars.size(); i++) {
            Field.render(g, scale, avatars.get(i));
        }
        Avatar avatar = avatars.get(0);
        if (avatar != null) {
            double x = avatar.getPosX();
            double y = avatar.getPosY();
            int r = (int) avatar.getRadius();
            Font font = new Font("Calibri", Font.BOLD, r / 2);
            FontMetrics metrics = g.getFontMetrics(font);
            int xt = (int) x - metrics.stringWidth(avatar.getNickName()) / 2;
            int yt = (int) (y + r / 4);
            g.setFont(font);
            g.drawString(avatar.getNickName(), xt, yt);
        }

    }*/

    public ArrayList<Circle> getTop() {

        ArrayList<Circle> top = new ArrayList<>();
        top = circles;
        top.sort(new Comparator<Circle>() {
            @Override
            public int compare(Circle a1, Circle a2) {
                return (int) (a2.getRadius() - a1.getRadius());
            }

        });

        if (top.size() > 3) {
            top.subList(3, top.size()).clear();
            return top;
        } else {
            return top;
        }
    }

    /*public String reportScores() {

        String report = "";

        for (int i = 0; i < getTop().size(); i++) {
            DecimalFormat df = new DecimalFormat("#.##");
            report += (i + 1) + ".  " + getTop().get(i).getNickName() + " " + df.format(getTop().get(i).getRadius()) + "\r\n";
        }
        return report;
    }*/

    public void initializeWorld(ArrayList<Circle> players, ArrayList<Circle> food) {
        this.circles = players;
        this.food = food;
    }

    public void updateWorld(ArrayList<Circle> players, ArrayList<Circle> food, int id) {
        Circle local = null;

        for (Circle circle : this.circles) {
            if (circle.getId() == id) {
                local = circle;
            }
        }

        this.food = food;
        this.circles = players;

        for (Circle circle : circles) {
            if (circle.getId() == id) {
                circle.setPosX(local.getPosX());
                circle.setPosY(local.getPosY());
            }
        }
    }

    public void updatePlayer(int id, double x, double y, boolean isAlive, double rad) {
        Circle a = getAvatar(id);
        a.setPosX(x);
        a.setPosY(y);
        a.setAlive(isAlive);
        if (a.getRadius() < rad) {
            a.setRadius(rad);
        }
    }

    public boolean calculateWin(int id) {
        return (getTop().get(0).getId() == id);
    }

}
