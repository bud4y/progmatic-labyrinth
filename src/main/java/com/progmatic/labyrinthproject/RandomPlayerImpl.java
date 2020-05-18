package com.progmatic.labyrinthproject;

import com.progmatic.labyrinthproject.enums.Direction;
import com.progmatic.labyrinthproject.interfaces.Labyrinth;
import com.progmatic.labyrinthproject.interfaces.Player;
import java.util.List;
import java.util.Random;

public class RandomPlayerImpl implements Player {
    @Override
    public Direction nextMove(Labyrinth l) {
        List<Direction> directions = l.possibleMoves();
        Random random = new Random();
        return directions.get(random.nextInt(directions.size()));
    }
}
