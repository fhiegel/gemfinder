package com.dedale.character;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;

import org.junit.Before;
import org.junit.Test;

import com.dedale.world.Mountain;
import com.dedale.world.Position;
import com.dedale.world.World;
import com.dedale.world.cartesian.CartesianOrientation;
import com.dedale.world.cartesian.CartesianPosition;
import com.dedale.world.cartesian.CartesianWorld;

public class PlayerCharacterTest {
    
    private CartesianPosition INITIAL_POSITION;
    private World WORLD;
    
    @Before
    public void initializeWorld() {
        WORLD = new CartesianWorld();
        INITIAL_POSITION = cartesian(1, 1);
    }
    
    @Test
    public void character_should_move_to_north_when_facing_north() throws Exception {
        assertMoveForward(CartesianOrientation.NORTH, cartesian(1, 2));
    }
    
    @Test
    public void character_should_move_to_west_when_facing_west() throws Exception {
        assertMoveForward(CartesianOrientation.WEST, cartesian(0, 1));
    }
    
    @Test
    public void character_should_move_to_SOUTH_when_facing_SOUTH() throws Exception {
        assertMoveForward(CartesianOrientation.SOUTH, cartesian(1, 0));
    }
    
    @Test
    public void character_should_move_to_east_when_facing_east() throws Exception {
        assertMoveForward(CartesianOrientation.EAST, cartesian(2, 1));
    }
    
    @Test
    public void character_should_turn_clockwise_when_facing_north() throws Exception {
        assertTurnClockWise(CartesianOrientation.NORTH, CartesianOrientation.EAST);
    }
    
    @Test
    public void character_should_turn_clockwise_when_facing_east() throws Exception {
        assertTurnClockWise(CartesianOrientation.EAST, CartesianOrientation.SOUTH);
    }
    
    @Test
    public void character_should_turn_clockwise_when_facing_south() throws Exception {
        assertTurnClockWise(CartesianOrientation.SOUTH, CartesianOrientation.WEST);
    }
    
    @Test
    public void character_should_turn_clockwise_when_facing_west() throws Exception {
        assertTurnClockWise(CartesianOrientation.WEST, CartesianOrientation.NORTH);
    }
    
    @Test
    public void character_should_turn_counter_clockwise_when_facing_north() throws Exception {
        assertTurnCounterClockWise(CartesianOrientation.NORTH, CartesianOrientation.WEST);
    }
    
    @Test
    public void character_should_turn_counter_clockwise_when_facing_west() throws Exception {
        assertTurnCounterClockWise(CartesianOrientation.WEST, CartesianOrientation.SOUTH);
    }
    
    @Test
    public void character_should_turn_counter_clockwise_when_facing_south() throws Exception {
        assertTurnCounterClockWise(CartesianOrientation.SOUTH, CartesianOrientation.EAST);
    }
    
    @Test
    public void character_should_turn_counter_clockwise_when_facing_east() throws Exception {
        assertTurnCounterClockWise(CartesianOrientation.EAST, CartesianOrientation.NORTH);
    }
    
    @Test
    public void character_should_not_move_on_position_containing_a_mountain() throws Exception {
        // Arrange
        PlayerCharacter playerCharacter = new PlayerCharacter(CartesianOrientation.NORTH);
        playerCharacter.addAvailableAction(new Move(WORLD));
        
        WORLD.at(1, 2).addLocalizable(new Mountain());
        WORLD.at(INITIAL_POSITION).addLocalizable(playerCharacter);
        
        // Act
        playerCharacter.move("M");
        Position finalPosition = WORLD.positionOf(playerCharacter);
        
        // Assert
        assertThat(finalPosition).isEqualTo(INITIAL_POSITION);
    }
    
    // Utilitaires
    
    private CartesianPosition cartesian(int x, int y) {
        return CartesianPosition.of(x, y);
    }
    
    private void assertMoveForward(CartesianOrientation initialOrientation, CartesianPosition expectedFinalPosition) {
        assertCharacterMove("M", INITIAL_POSITION, initialOrientation, expectedFinalPosition, initialOrientation);
    }
    
    private void assertTurnClockWise(CartesianOrientation initialOrientation, CartesianOrientation expectedFinalOrientation) {
        assertCharacterMove("R", INITIAL_POSITION, initialOrientation, INITIAL_POSITION, expectedFinalOrientation);
    }
    
    private void assertTurnCounterClockWise(CartesianOrientation initialOrientation, CartesianOrientation expectedFinalOrientation) {
        assertCharacterMove("L", INITIAL_POSITION, initialOrientation, INITIAL_POSITION, expectedFinalOrientation);
    }
    
    private void assertCharacterMove(String move, Position initialPosition, CartesianOrientation initialOrientation,
            CartesianPosition expectedFinalPosition, CartesianOrientation expectedFinalOrientation) {
        // Arrange
        PlayerCharacter playerCharacter = new PlayerCharacter(initialOrientation);
        playerCharacter.addAvailableAction(new Move(WORLD));
        WORLD.at(initialPosition).addLocalizable(playerCharacter);
        
        // Act
        playerCharacter.move(move);
        Position finalPosition = WORLD.positionOf(playerCharacter);
        
        // Assert
        assertThat(finalPosition.toString()).isEqualTo(expectedFinalPosition.toString());
        assertThat(getInternalState(playerCharacter, "orientation")).isEqualTo(expectedFinalOrientation);
    }
    
}