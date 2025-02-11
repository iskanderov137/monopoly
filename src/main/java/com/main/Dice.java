package com.main;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Dice {
    private static class DicePosition {
        int x, y;

        DicePosition(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private final double startX = 300;
    private final double startY = -200;
    private final double startRotateX = 0;
    private final double startRotateY = 0;
    private final Random random;
    private static List<DicePosition> occupiedPositions = new ArrayList<>();
    private DicePosition position;

    public Group group;

    private final int minW;
    private final int minH;
    private final int maxW;
    private final int maxH;

    private static final String img1 = Objects.requireNonNull(Dice.class.getResource("/imgs/dice1.png")).toString();
    private static final String img2 = Objects.requireNonNull(Dice.class.getResource("/imgs/dice2.png")).toString();
    private static final String img3 = Objects.requireNonNull(Dice.class.getResource("/imgs/dice3.png")).toString();
    private static final String img4 = Objects.requireNonNull(Dice.class.getResource("/imgs/dice4.png")).toString();
    private static final String img5 = Objects.requireNonNull(Dice.class.getResource("/imgs/dice5.png")).toString();
    private static final String img6 = Objects.requireNonNull(Dice.class.getResource("/imgs/dice6.png")).toString();

    public int value = 0;


    Dice(int minW, int maxW, int minH, int maxH) {
        this.minH = minH;
        this.minW = minW;
        this.maxH = maxH;
        this.maxW = maxW;
        Box box = createDice();
        this.group = new Group(box);
        this.random = new Random();

        placeTextureOnFace(group, img2, 0, 0, 25, 0, 0);
        placeTextureOnFace(group, img4, 0, 25, 0, -90, 0);
        placeTextureOnFace(group, img3, 0, -25, 0, 90, 0);
        placeTextureOnFace(group, img5, 0, 0, -25, 180, 0);
        placeTextureOnFace(group, img6, 25, 0, 0, 90, 90);
        placeTextureOnFace(group, img1, -25, 0, 0, -90, 90);
        group.setTranslateX(startX);
        group.setTranslateY(startY);
    }

    public void throwDice() {
        this.position = generateRandomPosition();
        value = random.nextInt(6) + 1;
//        System.out.println("Кубик показує: " + value);

        Timeline rotation = createRotationAnimation();
        Timeline drop = dropAnimation();

        rotation.setOnFinished(event -> resetToStartPosition());

        Dice.occupiedPositions = new ArrayList<>();

        drop.play();
        rotation.play();
    }


    private boolean isPositionOccupied(DicePosition position) {
        for (DicePosition p : occupiedPositions) {
            if (Math.abs(p.x - position.x) < 100 && Math.abs(p.y - position.y) < 100) {
                return true;
            }
        }
        return false;
    }

    private DicePosition generateRandomPosition() {
        int x = random.ints(minW, maxW).findFirst().getAsInt();
        int y = random.ints(minH, maxH).findFirst().getAsInt();
        DicePosition pos = new DicePosition(x, y);
        while (isPositionOccupied(pos)) {
            if(random.ints(minW, maxW).findFirst().isPresent()){
                x = random.ints(minW, maxW).findFirst().getAsInt();
            }
            if(random.ints(minH, maxH).findFirst().isPresent()){
                x = random.ints(minH, maxH).findFirst().getAsInt();
            }
            pos = new DicePosition(x, y);
        }
        occupiedPositions.add(pos);
        return pos;
    }

    private Timeline createRotationAnimation() {
        Rotate rotateX = new Rotate(0, new Point3D(1, 0, 0));
        Rotate rotateY = new Rotate(0, new Point3D(0, 1, 0));
        Rotate rotateZ = new Rotate(0, new Point3D(0, 0, 1));

        group.getTransforms().addAll(rotateX, rotateY, rotateZ);
        int y = 0;
        int x = 0;
        if (value == 1) {
            y = -450;
        }
        if (value == 2) {
            y = 540;
        }
        if (value == 3) {
            x = 450;
        }
        if (value == 4) {
            x = -450;
        }
        if (value == 6) {
            y = 450;
        }
        Timeline rotationAnimation = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(rotateX.angleProperty(), 0),
                        new KeyValue(rotateY.angleProperty(), 0),
                        new KeyValue(rotateZ.angleProperty(), 0)
                ),
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(rotateX.angleProperty(), x),
                        new KeyValue(rotateY.angleProperty(), y)
                )
        );

        rotationAnimation.setCycleCount(1);
        return rotationAnimation;
    }

    private void resetToStartPosition() {
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> {
            Rotate rotateX = new Rotate(startRotateX, new Point3D(1, 0, 0));
            Rotate rotateY = new Rotate(startRotateY, new Point3D(0, 1, 0));

            group.getTransforms().clear(); // Очищення попередніх трансформацій
            group.getTransforms().addAll(rotateX, rotateY);

            Timeline resetAnimation = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(group.translateXProperty(), startX),
                            new KeyValue(group.translateYProperty(), startY)
                    ),
                    new KeyFrame(Duration.seconds(0.1),
                            new KeyValue(group.translateXProperty(), startX),
                            new KeyValue(group.translateYProperty(), startY)
                    )
            );

            resetAnimation.setCycleCount(1);
            resetAnimation.play();
        });
        pause.play();
    }

    private Timeline dropAnimation() {
        Timeline fallAnimation = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(group.translateYProperty(), -200)
                ),
                new KeyFrame(Duration.ZERO,
                        new KeyValue(group.translateXProperty(), 200)
                ),
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(group.translateYProperty(), position.y)
                ),
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(group.translateXProperty(),
                                position.x)
                )
        );
        fallAnimation.setCycleCount(1);
        return fallAnimation;
    }

    private Box createDice() {
        Box dice = new Box(50, 50, 50);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.WHITE);
        dice.setMaterial(material);
        return dice;
    }

    private void placeTextureOnFace(Group parent, String path, double x, double y, double z, double rx, double ry) {
        Image textureImage = new Image(path);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(textureImage);
        Box textureNode = new Box(50, 50, 2);
        textureNode.setMaterial(material);
        Group textureGroup = new Group(textureNode);
        textureGroup.getTransforms().addAll(
                new Translate(x, y, z),
                new Rotate(rx, new Point3D(1, 0, 0)),
                new Rotate(ry, new Point3D(0, 1, 0)),
                new Rotate(0, new Point3D(0, 0, 1))
        );
        parent.getChildren().add(textureGroup);
    }
}
