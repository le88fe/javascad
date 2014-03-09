package eu.printingin3d.javascad.models;

import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundary;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.coords.Dims3d;
import eu.printingin3d.javascad.enums.AlignType;
import eu.printingin3d.javascad.enums.Side;

/**
 * Represents a cuboid. 
 *
 * @author ivivan <ivivan@printingin3d.eu>
 */
public class Cube extends Abstract3dModel {
	private final Dims3d size;
	
	/**
	 * Creates a cuboid with the given corners
	 * @param minCorner the corner with the lower coordinates
	 * @param maxCorner the corner with the higher coordinates 
	 * @throws eu.printingin3d.javascad.exceptions.IllegalValueException the minCorner has bigger value 
	 * 			then maxCorner in any coordinate (x, y or z)
	 */
	public Cube(Coords3d minCorner, Coords3d maxCorner) {
		this(new Dims3d(
				maxCorner.getX()-minCorner.getX(), 
				maxCorner.getY()-minCorner.getY(), 
				maxCorner.getZ()-minCorner.getZ()));
		align(new Side(AlignType.MIN, AlignType.MIN, AlignType.MIN), minCorner);
	}
	
	/**
	 * Creates a cuboid with the given size values.
	 * @param size the 3D size value used to construct the cuboid
	 */
	public Cube(Dims3d size) {
		super();
		this.size = size;
	}
	
	/**
	 * Creates a cube with the given size.
	 * @param size the size used to construct the cube
	 */
	public Cube(double size) {
		super();
		this.size = new Dims3d(size, size, size);
	}

	@Override
	protected String innerToScad() {
		return "cube("+size+",center=true);\n";
	}

	@Override
	protected Boundaries3d getModelBoundaries() {
		double x = size.getX()/2.0;
		double y = size.getY()/2.0;
		double z = size.getZ()/2.0;
		return new Boundaries3d(
				new Boundary(-x, x),
				new Boundary(-y, y),
				new Boundary(-z, z));
	}

	@Override
	protected Abstract3dModel innerCloneModel() {
		return new Cube(size);
	}
}