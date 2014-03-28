package io.github.ryanfb.georefine;

import java.util.Properties;

import org.json.JSONException;
import org.json.JSONWriter;

import com.google.refine.expr.EvalError;
import com.google.refine.grel.ControlFunctionRegistry;
import com.google.refine.grel.Function;

import org.geotools.geometry.GeometryBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.primitive.PrimitiveFactory;

import org.geotools.geometry.jts.JTS;
import com.vividsolutions.jts.geom.Coordinate;
import org.geotools.referencing.GeodeticCalculator;

import com.vividsolutions.jts.geom.Geometry;

public class Distance implements Function {

    public Object call(Properties bindings, Object[] args) {
        if(args.length==2){
            try {
                return JTS.orthodromicDistance(
                    ((com.vividsolutions.jts.geom.Point)args[0]).getCoordinate(),
                    ((com.vividsolutions.jts.geom.Point)args[1]).getCoordinate(),
                    DefaultGeographicCRS.WGS84);
            }
            catch (org.opengis.referencing.operation.TransformException e) {
                return new EvalError(ControlFunctionRegistry.getFunctionName(this) + ": TransformException: " + e.getMessage());
            }
        }
        return new EvalError(ControlFunctionRegistry.getFunctionName(this) + " expects 2 arguments");
    }

    public void write(JSONWriter writer, Properties options)
            throws JSONException {
        writer.object();
        writer.key("description"); writer.value("computes distance between coordinates");
        writer.key("params"); writer.value("point1, point2");
        writer.key("returns"); writer.value("distance in meters");
        writer.endObject();
    }

}
