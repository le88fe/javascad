package eu.printingin3d.javascad.testutils;

import eu.printingin3d.javascad.context.IScadGenerationContext;
import eu.printingin3d.javascad.coords2d.Boundaries2d;
import eu.printingin3d.javascad.coords2d.Coords2d;
import eu.printingin3d.javascad.models.SCAD;
import eu.printingin3d.javascad.models2d.Abstract2dModel;
import eu.printingin3d.javascad.vrl.CSG;
import eu.printingin3d.javascad.vrl.FacetGenerationContext;

public class Test2dModel extends Abstract2dModel {
	private final String model;
	private final Boundaries2d boundaries;

	private Test2dModel(Coords2d move, String model, Boundaries2d boundaries) {
		super(move);
		this.model = model;
		this.boundaries = boundaries;
	}
	
	public Test2dModel(String model, Boundaries2d boundaries) {
		this(Coords2d.ZERO, model, boundaries);
	}
	
	@Override
	protected SCAD innerToScad(IScadGenerationContext context) {
		return new SCAD(model);
	}

	@Override
	protected Boundaries2d getModelBoundaries() {
		return boundaries;
	}

	@Override
	public CSG toCSG(FacetGenerationContext context) {
		return null;
	}

	@Override
	public Abstract2dModel move(Coords2d delta) {
		return new Test2dModel(move.move(delta), this.model, this.boundaries);
	}
}
