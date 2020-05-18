package com.progmatic.labyrinthproject;

import com.progmatic.labyrinthproject.enums.CellType;
import com.progmatic.labyrinthproject.enums.Direction;
import com.progmatic.labyrinthproject.exceptions.CellException;
import com.progmatic.labyrinthproject.exceptions.InvalidMoveException;
import com.progmatic.labyrinthproject.interfaces.Labyrinth;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author pappgergely
 */
public class LabyrinthImpl implements Labyrinth {
    private CellType[][] labyrinth;
    private Coordinate playerPos;
    private int width = -1;
    private int height = -1;

    public LabyrinthImpl() {
        playerPos = new Coordinate(0, 0);
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public void loadLabyrinthFile(String fileName) {
        try {
            Scanner sc = new Scanner(new File(fileName));
            int width = Integer.parseInt(sc.nextLine());
            int height = Integer.parseInt(sc.nextLine());
            setSize(width, height);
            for (int hh = 0; hh < height; hh++) {
                String line = sc.nextLine();
                for (int ww = 0; ww < width; ww++) {
                    switch (line.charAt(ww)) {
                        case 'W':
                            labyrinth[ww][hh] = CellType.WALL;
                            break;
                        case 'E':
                            labyrinth[ww][hh] = CellType.END;
                            break;
                        case 'S':
                            labyrinth[ww][hh] = CellType.START;
                            playerPos = new Coordinate(ww, hh);
                            break;
                        default:
                            labyrinth[ww][hh] = CellType.EMPTY;
                    }
                }
            }
        } catch (FileNotFoundException | NumberFormatException ex) {
            System.out.println(ex.toString());
        }
    }

    @Override
    public CellType getCellType(Coordinate c) throws CellException {
        if (c.getRow() >= height || c.getRow() < 0 || c.getCol() >= width || c.getCol() < 0) {
            throw new CellException(c, "Helytelenül megadott coordináta!");
        }
        return labyrinth[c.getCol()][c.getRow()];
    }

    @Override
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        labyrinth = new CellType[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                labyrinth[j][i] = CellType.EMPTY;
            }
        }
    }


    @Override
    public void setCellType(Coordinate c, CellType type) throws CellException {
        if (!(c.getRow() >= height || c.getRow() < 0 || c.getCol() >= width || c.getCol() < 0)) {
            labyrinth[c.getCol()][c.getRow()] = type;
            if (type == CellType.START)
                playerPos = c;
        } else {
            throw new CellException(c, "Helytelenül megadott coordináta!");
        }
    }

    @Override
    public Coordinate getPlayerPosition() {
        return playerPos;
    }

    @Override
    public boolean hasPlayerFinished() {
        return labyrinth[playerPos.getCol()][playerPos.getRow()] == CellType.END;
    }

    @Override
    public List<Direction> possibleMoves() {
        List<Direction> possibleDirection = new ArrayList<>();

        try {
            if (playerPos.getCol() > 0 && getCellType(new Coordinate(playerPos.getCol() - 1, playerPos.getRow())) != CellType.WALL) {
                possibleDirection.add(Direction.WEST);
            }
            if (playerPos.getCol() < getWidth() - 1 && getCellType(new Coordinate(playerPos.getCol() + 1, playerPos.getRow())) != CellType.WALL) {
                possibleDirection.add(Direction.EAST);
            }
            if (playerPos.getRow() > 0 && getCellType(new Coordinate(playerPos.getCol(), playerPos.getRow() - 1)) != CellType.WALL) {
                possibleDirection.add(Direction.NORTH);
            }
            if (playerPos.getRow() + 1 < getHeight() && getCellType(new Coordinate(playerPos.getCol(), playerPos.getRow() + 1)) != CellType.WALL) {
                possibleDirection.add(Direction.SOUTH);
            }
        } catch (CellException ex) {
            System.out.println("Hibás irány!");
        }
        return possibleDirection;
    }


    @Override
    public void movePlayer(Direction direction) throws InvalidMoveException {

        switch (direction) {
            case SOUTH:
                if (playerPos.getRow() + 1 >= labyrinth.length || labyrinth[playerPos.getCol()][playerPos.getRow() + 1] == CellType.WALL) {
                    throw new InvalidMoveException();
                } else {
                    playerPos = new Coordinate(playerPos.getCol(), playerPos.getRow() + 1);
                    break;
                }

            case NORTH:
                if (playerPos.getRow() - 1 < 0 || labyrinth[playerPos.getCol()][playerPos.getRow() - 1] == CellType.WALL) {
                    throw new InvalidMoveException();
                } else {
                    playerPos = new Coordinate(playerPos.getCol(), playerPos.getRow() - 1);
                    break;
                }

            case EAST:
                if (playerPos.getCol() + 1 > labyrinth[playerPos.getRow()].length || labyrinth[playerPos.getCol() + 1][playerPos.getRow()] == CellType.WALL) {
                    throw new InvalidMoveException();
                } else {
                    playerPos = new Coordinate(playerPos.getCol() + 1, playerPos.getRow());
                    break;
                }

            case WEST:
                if (playerPos.getCol() - 1 < 0 || labyrinth[playerPos.getCol() - 1][playerPos.getRow()] == CellType.WALL) {
                    throw new InvalidMoveException();
                } else {
                    playerPos = new Coordinate(playerPos.getCol() - 1, playerPos.getRow());
                    break;
                }
        }
    }
}
