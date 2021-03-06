package eu.printingin3d.javascad.models;

import static eu.printingin3d.javascad.testutils.AssertEx.assertEqualsWithoutWhiteSpaces;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import eu.printingin3d.javascad.context.ScadGenerationContextFactory;
import eu.printingin3d.javascad.coords.Angles3d;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.enums.Side;
import eu.printingin3d.javascad.testutils.Test3dModel;

public class Abstract3dModelTest {
	private static final double MAX_BOUND = 15.0;
	private static final double MIN_BOUND = -10.0;
	private static final Boundaries3d BOUNDARIES = new Boundaries3d(
			new Coords3d(MIN_BOUND, MIN_BOUND, MIN_BOUND), 
			new Coords3d(MAX_BOUND, MAX_BOUND, MAX_BOUND));
	
	private Abstract3dModel testSubject;
	
	@Before
	public void init() {
		testSubject = new Test3dModel("(empty)", BOUNDARIES);
	}
	
	@Test
	public void testDefault() {
		assertEqualsWithoutWhiteSpaces("(empty)", testSubject.toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}
	
	@Test
	public void testDefaultWithDebug() {
		testSubject.debug();
		assertEqualsWithoutWhiteSpaces("# (empty)", testSubject.toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}
	
	@Test
	public void testDefaultWithBackground() {
		testSubject.background();
		assertEqualsWithoutWhiteSpaces("% (empty)", testSubject.toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}
	
	@Test
	public void testDefaultWithDebugAndBackground() {
		testSubject.background().debug();
		assertEqualsWithoutWhiteSpaces("# % (empty)", testSubject.toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}
	
	@Test
	public void testMove() {
		testSubject.move(new Coords3d(10.0, 20.0, 30.0));
		assertEqualsWithoutWhiteSpaces("translate([10,20,30])(empty)", 
				testSubject.toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}

	@Test
	public void testMoves() {
		testSubject.moves(Arrays.asList(new Coords3d(10.0, 20.0, 30.0), new Coords3d(30.0, 10.0, 20.0)));
		assertEqualsWithoutWhiteSpaces("union(){translate([10,20,30])(empty)translate([30,10,20])(empty)}", 
				testSubject.toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}
	
	@Test
	public void movesWithEmptyListDoesNothing() {
		testSubject.moves(Collections.<Coords3d>emptyList());
		assertEqualsWithoutWhiteSpaces("(empty)", testSubject.toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}
	
	@Test
	public void testMovesWithDebug() {
		testSubject.moves(Arrays.asList(new Coords3d(10.0, 20.0, 30.0), new Coords3d(30.0, 10.0, 20.0)));
		testSubject.debug();
		assertEqualsWithoutWhiteSpaces("# union(){translate([10,20,30])(empty)translate([30,10,20])(empty)}", 
				testSubject.toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}

	@Test
	public void testRotate() {
		testSubject.rotate(new Angles3d(10.0, 20.0, 30.0));
		assertEqualsWithoutWhiteSpaces("rotate([10,20,30]) (empty)", 
				testSubject.toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}
	
	@Test
	public void testMoveAndRotate() {
		testSubject.rotate(new Angles3d(10.0, 20.0, 30.0));
		testSubject.move(new Coords3d(30.0, 10.0, 20.0));
		assertEqualsWithoutWhiteSpaces("translate([30,10,20]) rotate([10,20,30]) (empty)", 
				testSubject.toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}
	
	@Test
	public void testRotateAndMove() {
		testSubject.move(Coords3d.yOnly(30.0));
		testSubject.rotate(Angles3d.ROTATE_PLUS_X);
		assertEqualsWithoutWhiteSpaces("translate([0,0,30]) rotate([90,0,0]) (empty)", 
				testSubject.toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}
	
	@Test
	public void testMoveRotateAndMove() {
		testSubject.move(Coords3d.yOnly(30.0));
		testSubject.rotate(Angles3d.ROTATE_PLUS_X);
		testSubject.move(new Coords3d(30.0, 10.0, 20.0));
		assertEqualsWithoutWhiteSpaces("translate([30,10,50]) rotate([90,0,0]) (empty)", 
				testSubject.toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}
	
	@Test
	public void testMovesAndRotate() {
		testSubject.rotate(new Angles3d(10.0, 20.0, 30.0));
		testSubject.moves(Arrays.asList(new Coords3d(10.0, 20.0, 30.0), new Coords3d(30.0, 10.0, 20.0)));
		assertEqualsWithoutWhiteSpaces("union(){translate([10,20,30]) rotate([10,20,30]) (empty) translate([30,10,20]) rotate([10,20,30]) (empty)}", 
				testSubject.toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}
	
	@Test
	public void testAlignTop() {
		Cube cube1 = new Cube(30.0);
		testSubject.align(Side.TOP, cube1, false);
		// moves up with half of the cube -> 30/2=15 + 10, because the testSubject's bottom is at that point
		assertEqualsWithoutWhiteSpaces("translate([0, 0, 25]) (empty)", 
				testSubject.toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}
	
	@Test
	public void testAlignBottom() {
		Cube cube1 = new Cube(30.0);
		testSubject.align(Side.BOTTOM, cube1, false);
		// moves down with half of the cube -> -30/2=-15 - 15, because the testSubject's top is at that point
		assertEqualsWithoutWhiteSpaces("translate([0, 0, -30]) (empty)", 
				testSubject.toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}
	
	@Test
	public void testAlignRight() {
		Cube cube1 = new Cube(30.0);
		testSubject.align(Side.RIGHT, cube1, false);
		// moves right with half of the cube -> 30/2=15 + 10, because the testSubject's left side is at that point
		assertEqualsWithoutWhiteSpaces("translate([25, 0, 0]) (empty)", 
				testSubject.toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}
	
	@Test
	public void testAlignLeft() {
		Cube cube1 = new Cube(30.0);
		testSubject.align(Side.LEFT, cube1, false);
		// moves right with half of the cube -> -30/2=-15 - 15, because the testSubject's top is at that point
		assertEqualsWithoutWhiteSpaces("translate([-30, 0, 0]) (empty)", 
				testSubject.toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}
	
	@Test
	public void testAlignBack() {
		Cube cube1 = new Cube(30.0);
		testSubject.align(Side.BACK, cube1, false);
		// moves back with half of the cube -> 30/2=15 + 10, because the testSubject's front side is at that point
		assertEqualsWithoutWhiteSpaces("translate([0, 25, 0]) (empty)", 
				testSubject.toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}
	
	@Test
	public void testAlignFront() {
		Cube cube1 = new Cube(30.0);
		testSubject.align(Side.FRONT, cube1, false);
		// moves front with half of the cube -> -30/2=-15 - 15, because the testSubject's back is at that point
		assertEqualsWithoutWhiteSpaces("translate([0, -30, 0]) (empty)", 
				testSubject.toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}
	
	@Test
	public void addModelShouldResultUnion() {
		assertEqualsWithoutWhiteSpaces("union() {(empty) (added)}",
				testSubject.addModel(new Test3dModel("(added)")).toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}
	
	@Test
	public void addModelShouldReturnNewObject() {
		Assert.assertNotSame(testSubject, testSubject.addModel(new Test3dModel("(added)")));
	}
	
	@Test
	public void addModelToShouldResultUnion() {
		assertEqualsWithoutWhiteSpaces("union() {(empty) translate("+Coords3d.zOnly(MAX_BOUND-MIN_BOUND)+")(added)}",
				testSubject.addModelTo(Side.TOP, 
						new Test3dModel("(added)", BOUNDARIES))
					.toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}
	
	@Test
	public void addModelToShouldReturnNewObject() {
		Abstract3dModel newUnion = testSubject.addModelTo(Side.TOP, new Test3dModel("(added)", 
				new Boundaries3d(new Coords3d(-5, -10, -20), new Coords3d(5, 10, 20))));
		Assert.assertNotSame(testSubject, newUnion);
	}
	
	@Test
	public void subtractModelShouldResultDifference() {
		assertEqualsWithoutWhiteSpaces("difference() {(empty) (added)}",
				testSubject.subtractModel(new Test3dModel("(added)")).toScad(ScadGenerationContextFactory.DEFAULT).getScad());
	}
	
	@Test
	public void subtrackModelShouldReturnNewObject() {
		Assert.assertNotSame(testSubject, testSubject.subtractModel(new Test3dModel("(added)")));
	}
}
