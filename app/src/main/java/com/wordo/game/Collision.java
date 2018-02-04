package com.wordo.game;

/**
 * Created by Arihant on 19-06-2016.
 */
public class Collision {

    public Boolean intersect(SensorBall sensorBall, AlphabetBall ball) {
        return (sensorBall != null && ball != null && Math.pow((ball.radius), 2) >= (Math.pow((ball.positionX - sensorBall.positionX), 2) + Math.pow((ball.positionY - sensorBall.positionY), 2)));
    }

    public Boolean intersect(SensorBall sensorBall, AngryBall ball) {
        return (sensorBall != null && ball != null && Math.pow((ball.radius + sensorBall.radius), 2) >= (Math.pow((ball.positionX - sensorBall.positionX), 2) + Math.pow((ball.positionY - sensorBall.positionY), 2)));
    }

    public Boolean intersect(SensorBall sensorBall, MagnetBall ball) {
        return (sensorBall != null && ball != null && Math.pow((ball.radius + sensorBall.radius), 2) >= (Math.pow((ball.positionX - sensorBall.positionX), 2) + Math.pow((ball.positionY - sensorBall.positionY), 2)));
    }

    public Boolean intersect(SensorBall sensorBall, Pit pitObj) {
        return (pitObj != null && sensorBall != null && Math.pow((pitObj.radius), 2) >= (Math.pow(sensorBall.positionX - pitObj.positionX, 2) + Math.pow(sensorBall.positionY - pitObj.positionY, 2)));
    }

    public boolean isCollisionPossible(float x1, float y1, float radius1, float x2, float y2, float radius2) {
        return (Math.pow((radius1 + radius2), 2) >= (Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2)));
    }

    //Jeannie's Charm
    public boolean isPositionProper(float x1, float y1, float x2, float y2, float expectedSeparatedDistance) {
        return (Math.pow((expectedSeparatedDistance), 2) <= (Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2)));
    }

    public void intersect(AlphabetBall ballA, AlphabetBall ballB) {
        if (ballA != null && ballB != null) {
            if (Math.pow((ballB.radius + ballA.radius), 2) >= (Math.pow((ballB.positionX - ballA.positionX), 2) + Math.pow((ballB.positionY - ballA.positionY), 2))) {
                double tempSpeedX = ballB.speedX;
                double tempSpeedY = ballB.speedY;

                double massOne = (4.0 / 3) * Math.PI * Math.pow(ballB.radius, 3), massAnother = (4.0 / 3) * Math.PI * Math.pow(ballA.radius, 3);

                ballB.speedX = (float) (((2 * massAnother * ballA.speedX) + (massOne - massAnother) * ballB.speedX) / (massOne + massAnother));
                ballB.speedY = (float) (((2 * massAnother * ballA.speedY) + (massOne - massAnother) * ballB.speedY) / (massOne + massAnother));

                ballA.speedX = (float) (((2 * massOne * tempSpeedX) + (massAnother - massOne) * ballA.speedX) / (massOne + massAnother));
                ballA.speedY = (float) (((2 * massOne * tempSpeedY) + (massAnother - massOne) * ballA.speedY) / (massOne + massAnother));

                ballB.positionX += (ballB.speedX * 0.1);
                ballB.positionY += (ballB.speedY * 0.1);

                ballA.positionX += (ballA.speedX * 0.1);
                ballA.positionY += (ballA.speedY * 0.1);
            }
        }
    }


    public void intersect(AngryBall ballA, AngryBall ballB) {
        if (ballA != null && ballB != null) {
            if (Math.pow((ballB.radius + ballA.radius), 2) >= (Math.pow((ballB.positionX - ballA.positionX), 2) + Math.pow((ballB.positionY - ballA.positionY), 2))) {
                double tempSpeedX = ballB.speedX;
                double tempSpeedY = ballB.speedY;

                double massOne = (4.0 / 3) * Math.PI * Math.pow(ballB.radius, 3), massAnother = (4.0 / 3) * Math.PI * Math.pow(ballA.radius, 3);

                ballB.speedX = (float) (((2 * massAnother * ballA.speedX) + (massOne - massAnother) * ballB.speedX) / (massOne + massAnother));
                ballB.speedY = (float) (((2 * massAnother * ballA.speedY) + (massOne - massAnother) * ballB.speedY) / (massOne + massAnother));

                ballA.speedX = (float) (((2 * massOne * tempSpeedX) + (massAnother - massOne) * ballA.speedX) / (massOne + massAnother));
                ballA.speedY = (float) (((2 * massOne * tempSpeedY) + (massAnother - massOne) * ballA.speedY) / (massOne + massAnother));

                ballB.positionX += (ballB.speedX * 0.1);
                ballB.positionY += (ballB.speedY * 0.1);

                ballA.positionX += (ballA.speedX * 0.1);
                ballA.positionY += (ballA.speedY * 0.1);
            }
        }
    }

    public void intersect(MagnetBall ballA, MagnetBall ballB) {
        if (ballA != null && ballB != null) {
            if (Math.pow((ballB.radius + ballA.radius), 2) >= (Math.pow((ballB.positionX - ballA.positionX), 2) + Math.pow((ballB.positionY - ballA.positionY), 2))) {
                double tempSpeedX = ballB.speedX;
                double tempSpeedY = ballB.speedY;

                double massOne = (4.0 / 3) * Math.PI * Math.pow(ballB.radius, 3), massAnother = (4.0 / 3) * Math.PI * Math.pow(ballA.radius, 3);

                ballB.speedX = (float) (((2 * massAnother * ballA.speedX) + (massOne - massAnother) * ballB.speedX) / (massOne + massAnother));
                ballB.speedY = (float) (((2 * massAnother * ballA.speedY) + (massOne - massAnother) * ballB.speedY) / (massOne + massAnother));

                ballA.speedX = (float) (((2 * massOne * tempSpeedX) + (massAnother - massOne) * ballA.speedX) / (massOne + massAnother));
                ballA.speedY = (float) (((2 * massOne * tempSpeedY) + (massAnother - massOne) * ballA.speedY) / (massOne + massAnother));

                ballB.positionX += (ballB.speedX * 0.1);
                ballB.positionY += (ballB.speedY * 0.1);

                ballA.positionX += (ballA.speedX * 0.1);
                ballA.positionY += (ballA.speedY * 0.1);
            }
        }
    }


    public void intersect(Room room, AlphabetBall ball) {
        if (room != null && ball != null) {
            ball.positionX += (ball.speedX * 0.1);
            ball.positionY += (ball.speedY * 0.1);

            if (ball.positionX <= (room.minimumX + (ball.radius))) {
                ball.speedX = -ball.speedX;
                ball.positionX = room.minimumX + (ball.radius);
            } else if (ball.positionX >= (room.maximumX - ball.radius)) {
                ball.speedX = -ball.speedX;
                ball.positionX = room.maximumX - ball.radius;
            }


            if (ball.positionY <= (room.minimumY + (ball.radius))) {
                ball.speedY = -ball.speedY;
                ball.positionY = room.minimumY + (ball.radius);
            } else if (ball.positionY >= (room.maximumY - ball.radius)) {
                ball.speedY = -ball.speedY;
                ball.positionY = room.maximumY - ball.radius;
            }
        }
    }

    public void intersect(Room room, AngryBall ball) {

        if (room != null && ball != null) {
            ball.positionX += (ball.speedX * 0.1);
            ball.positionY += (ball.speedY * 0.1);

            if (ball.positionX <= (room.minimumX + (ball.radius))) {
                ball.speedX = -ball.speedX;
                ball.positionX = room.minimumX + (ball.radius);
            } else if (ball.positionX >= (room.maximumX - ball.radius)) {
                ball.speedX = -ball.speedX;
                ball.positionX = room.maximumX - ball.radius;
            }


            if (ball.positionY <= (room.minimumY + (ball.radius))) {
                ball.speedY = -ball.speedY;
                ball.positionY = room.minimumY + (ball.radius);
            } else if (ball.positionY >= (room.maximumY - ball.radius)) {
                ball.speedY = -ball.speedY;
                ball.positionY = room.maximumY - ball.radius;
            }
        }
    }

    public void intersect(Room room, MagnetBall ball) {

        if (room != null && ball != null) {
            ball.positionX += (ball.speedX * 0.1);
            ball.positionY += (ball.speedY * 0.1);

            if (ball.positionX <= (room.minimumX + (ball.radius))) {
                ball.speedX = -ball.speedX;
                ball.positionX = room.minimumX + (ball.radius);
            } else if (ball.positionX >= (room.maximumX - ball.radius)) {
                ball.speedX = -ball.speedX;
                ball.positionX = room.maximumX - ball.radius;
            }


            if (ball.positionY <= (room.minimumY + (ball.radius))) {
                ball.speedY = -ball.speedY;
                ball.positionY = room.minimumY + (ball.radius);
            } else if (ball.positionY >= (room.maximumY - ball.radius)) {
                ball.speedY = -ball.speedY;
                ball.positionY = room.maximumY - ball.radius;
            }
        }
    }

    public void intersect(Pit pit, AlphabetBall ball) {
        if (pit != null && ball != null) {
            if (Math.pow((ball.radius + pit.radius), 2) >= (Math.pow((ball.positionX - pit.positionX), 2) + Math.pow((ball.positionY - pit.positionY), 2))) {

                ball.speedX = -ball.speedX;
                ball.speedY = -ball.speedY;

                ball.positionX += (ball.speedX * 0.1);
                ball.positionY += (ball.speedY * 0.1);

            }
        }
    }


    public void intersect(Pit pit, AngryBall ball) {

        if (pit != null && ball != null) {
            if (Math.pow((ball.radius + pit.radius), 2) >= (Math.pow((ball.positionX - pit.positionX), 2) + Math.pow((ball.positionY - pit.positionY), 2))) {

                ball.speedX = -ball.speedX;
                ball.speedY = -ball.speedY;

                ball.positionX += (ball.speedX * 0.1);
                ball.positionY += (ball.speedY * 0.1);

            }
        }
    }


    public void intersect(Pit pit, MagnetBall ball) {

        if (pit != null && ball != null) {
            if (Math.pow((ball.radius + pit.radius), 2) >= (Math.pow((ball.positionX - pit.positionX), 2) + Math.pow((ball.positionY - pit.positionY), 2))) {

                ball.speedX = -ball.speedX;
                ball.speedY = -ball.speedY;

                ball.positionX += (ball.speedX * 0.1);
                ball.positionY += (ball.speedY * 0.1);

            }
        }
    }

    public void intersect(AngryBall ballA, AlphabetBall ballB) {

        if (ballA != null && ballB != null) {
            if (Math.pow((ballB.radius + ballA.radius), 2) >= (Math.pow((ballB.positionX - ballA.positionX), 2) + Math.pow((ballB.positionY - ballA.positionY), 2))) {
                double tempSpeedX = ballB.speedX;
                double tempSpeedY = ballB.speedY;

                double massOne = (4.0 / 3) * Math.PI * Math.pow(ballB.radius, 3), massAnother = (4.0 / 3) * Math.PI * Math.pow(ballA.radius, 3);

                ballB.speedX = (float) (((2 * massAnother * ballA.speedX) + (massOne - massAnother) * ballB.speedX) / (massOne + massAnother));
                ballB.speedY = (float) (((2 * massAnother * ballA.speedY) + (massOne - massAnother) * ballB.speedY) / (massOne + massAnother));

                ballA.speedX = (float) (((2 * massOne * tempSpeedX) + (massAnother - massOne) * ballA.speedX) / (massOne + massAnother));
                ballA.speedY = (float) (((2 * massOne * tempSpeedY) + (massAnother - massOne) * ballA.speedY) / (massOne + massAnother));

                ballB.positionX += (ballB.speedX * 0.1);
                ballB.positionY += (ballB.speedY * 0.1);

                ballA.positionX += (ballA.speedX * 0.1);
                ballA.positionY += (ballA.speedY * 0.1);
            }
        }
    }

    public void intersect(AngryBall ballA, MagnetBall ballB) {

        if (ballA != null && ballB != null) {
            if (Math.pow((ballB.radius + ballA.radius), 2) >= (Math.pow((ballB.positionX - ballA.positionX), 2) + Math.pow((ballB.positionY - ballA.positionY), 2))) {
                double tempSpeedX = ballB.speedX;
                double tempSpeedY = ballB.speedY;

                double massOne = (4.0 / 3) * Math.PI * Math.pow(ballB.radius, 3), massAnother = (4.0 / 3) * Math.PI * Math.pow(ballA.radius, 3);

                ballB.speedX = (float) (((2 * massAnother * ballA.speedX) + (massOne - massAnother) * ballB.speedX) / (massOne + massAnother));
                ballB.speedY = (float) (((2 * massAnother * ballA.speedY) + (massOne - massAnother) * ballB.speedY) / (massOne + massAnother));

                ballA.speedX = (float) (((2 * massOne * tempSpeedX) + (massAnother - massOne) * ballA.speedX) / (massOne + massAnother));
                ballA.speedY = (float) (((2 * massOne * tempSpeedY) + (massAnother - massOne) * ballA.speedY) / (massOne + massAnother));

                ballB.positionX += (ballB.speedX * 0.1);
                ballB.positionY += (ballB.speedY * 0.1);

                ballA.positionX += (ballA.speedX * 0.1);
                ballA.positionY += (ballA.speedY * 0.1);
            }
        }
    }

    public void intersect(AlphabetBall ballA, MagnetBall ballB) {

        if (ballA != null && ballB != null) {
            if (Math.pow((ballB.radius + ballA.radius), 2) >= (Math.pow((ballB.positionX - ballA.positionX), 2) + Math.pow((ballB.positionY - ballA.positionY), 2))) {
                double tempSpeedX = ballB.speedX;
                double tempSpeedY = ballB.speedY;

                double massOne = (4.0 / 3) * Math.PI * Math.pow(ballB.radius, 3), massAnother = (4.0 / 3) * Math.PI * Math.pow(ballA.radius, 3);

                ballB.speedX = (float) (((2 * massAnother * ballA.speedX) + (massOne - massAnother) * ballB.speedX) / (massOne + massAnother));
                ballB.speedY = (float) (((2 * massAnother * ballA.speedY) + (massOne - massAnother) * ballB.speedY) / (massOne + massAnother));

                ballA.speedX = (float) (((2 * massOne * tempSpeedX) + (massAnother - massOne) * ballA.speedX) / (massOne + massAnother));
                ballA.speedY = (float) (((2 * massOne * tempSpeedY) + (massAnother - massOne) * ballA.speedY) / (massOne + massAnother));

                ballB.positionX += (ballB.speedX * 0.1);
                ballB.positionY += (ballB.speedY * 0.1);

                ballA.positionX += (ballA.speedX * 0.1);
                ballA.positionY += (ballA.speedY * 0.1);
            }
        }
    }
}
