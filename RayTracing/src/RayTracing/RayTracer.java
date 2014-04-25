package RayTracing;

import java.awt.Color;
import java.awt.Transparency;
import java.awt.color.*;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import Scene.*;
import Scene.Surface.*;

/**
 * Main class for ray tracing exercise.
 */
public class RayTracer
{

	public int imageWidth;
	public int imageHeight;

	/**
	 * Runs the ray tracer. Takes scene file, output image file and image size
	 * as input.
	 */
	public static void main(String[] args)
	{

		try
		{

			RayTracer tracer = new RayTracer();

			// Default values:
			tracer.imageWidth = 500;
			tracer.imageHeight = 500;

			if (args.length < 2)
				throw new RayTracerException(
						"Not enough arguments provided. Please specify an input scene file and an output image file for rendering.");

			String sceneFileName = args[0];
			String outputFileName = args[1];

			if (args.length > 3)
			{
				tracer.imageWidth = Integer.parseInt(args[2]);
				tracer.imageHeight = Integer.parseInt(args[3]);
			}

			// Parse scene file:
			tracer.parseScene(sceneFileName);

			// Render scene:
			tracer.renderScene(outputFileName);

			// } catch (IOException e) {
			// System.out.println(e.getMessage());
		}
		catch (RayTracerException e)
		{
			System.out.println(e.getMessage());
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}

	}

	/**
	 * Parses the scene file and creates the scene. Change this function so it
	 * generates the required objects.
	 */
	public void parseScene(String sceneFileName) throws IOException, RayTracerException
	{
		FileReader fr = new FileReader(sceneFileName);

		BufferedReader r = new BufferedReader(fr);
		String line = null;
		int lineNum = 0;
		System.out.println("Started parsing scene file " + sceneFileName);

		while ((line = r.readLine()) != null)
		{
			line = line.trim();
			++lineNum;

			if (line.isEmpty() || (line.charAt(0) == '#'))
			{ // This line in the scene file is a comment
				continue;
			}
			else
			{
				String code = line.substring(0, 3).toLowerCase();
				// Split according to white space characters:
				String[] params = line.substring(3).trim().toLowerCase().split("\\s+");
				List<Material> materials = new ArrayList<Material>();
				List<Surface> surfaces = new ArrayList<Surface>();
				List<Light> lights = new ArrayList<Light>();
				Scene scene = new Scene();
				
				if (code.equals("cam"))
				{
					/*
					 * "cam" = camera settings (there will be only one per scene
					 * file) params[0,1,2] = position (x, y, z) of the camera
					 * params[3,4,5] = look-at position (x, y, z) of the camera
					 * params[6,7,8] = up vector (x, y, z) of the camera
					 * params[9] = screen distance from camera 
					 * params[10] = screen width from camera
					 */

					if (params.length != 11)
					{
						throw new RayTracerException(
								"Incomapible number of parameters for camera.");

					}
					
					Camera camera = new Camera();
					camera.setPosition(new Vector(Double.parseDouble(params[0]), 
												  Double.parseDouble(params[1]), 
												  Double.parseDouble(params[2])));
					camera.setLookAtPosition(new Vector(Double.parseDouble(params[3]), 
														Double.parseDouble(params[4]), 
														Double.parseDouble(params[5])));
					camera.setUpVector(new Vector(Double.parseDouble(params[6]), 
												  Double.parseDouble(params[7]), 
												  Double.parseDouble(params[8])));
					camera.setScreenDist(Double.parseDouble(params[9]));
					camera.setScreenWidth(Double.parseDouble(params[10]));

					scene.setCamera(camera);
					
					System.out.println(String.format("Parsed camera parameters (line %d)", lineNum));
				}
				else if (code.equals("set"))
				{
					/*
					 * "set" = general settings for the scene (once per scene file) 
				 	 * 		params[0,1,2] = background color (r, g, b) 
				  	 * 		params[3] = root number of shadow rays (N^2 rays will be shot) 
				  	 * 		params[4] = maximum number of recursions 
					 */
					
					if (params.length != 5)
					{
						throw new RayTracerException(
								"Incomapible number of parameters for camera.");

					}
					
					scene.setBackground(new Color(Float.parseFloat(params[0]),
												  Float.parseFloat(params[1]),
												  Float.parseFloat(params[2])));
					scene.setNumOfShadowRays(Integer.parseInt(params[3]));
					scene.setMaxNumOfRecursions(Integer.parseInt(params[4]));
					
					System.out.println(String.format("Parsed general settings (line %d)", lineNum));
				}
				else if (code.equals("mtl"))
				{
					/*
					 * "mtl" = defines a new material 
				 	 * 		params[0,1,2] = diffuse color (r, g, b) 
				  	 * 		params[3,4,5] = specular color (r, g, b) 
				  	 * 		params[6,7,8] = reflection color (r, g, b) 
				  	 * 		params[9] = phong specularity coefficient (shininess) 
				  	 * 		params[10] = transparency value between 0 and 1 
					 */
					
					if (params.length != 11)
					{
						throw new RayTracerException(
								"Incomapible number of parameters for camera.");

					}
					
					Material material = new Material();
					
					material.setDiffuse(new Color(Float.parseFloat(params[0]), 
												  Float.parseFloat(params[1]), 
												  Float.parseFloat(params[2])));
					material.setSpecular(new Color(Float.parseFloat(params[3]), 
												   Float.parseFloat(params[4]), 
												   Float.parseFloat(params[5])));
					material.setReflection(new Color(Float.parseFloat(params[6]), 
													 Float.parseFloat(params[7]), 
													 Float.parseFloat(params[8])));
					material.setPhong(Double.parseDouble(params[9]));
					material.setTransparency(Double.parseDouble(params[10]));

					materials.add(material);
					
					System.out.println(String.format("Parsed material (line %d)", lineNum));
				}
				else if (code.equals("sph"))
				{

					/*
					 * "sph" = defines a new sphere 
				 	 * 		params[0,1,2] = position of the sphere center (x, y, z) 
				  	 * 		params[3] = radius 
				  	 * 		params[4] = material index (integer). each defined material gets an 
				  	 * 		automatic material index starting from 1, 2 and so on
					 */
					
					if (params.length != 5)
					{
						throw new RayTracerException(
								"Incomapible number of parameters for camera.");

					}
					Sphere sphere = new Sphere();
					
					sphere.setCenter(new Vector(Double.parseDouble(params[0]), 
												Double.parseDouble(params[1]), 
												Double.parseDouble(params[2])));
					sphere.setRadius(Double.parseDouble(params[3]));
					sphere.setMaterial(Integer.parseInt(params[4]) - 1);
					
					surfaces.add(sphere);
					
					System.out.println(String.format("Parsed sphere (line %d)", lineNum));
				}
				else if (code.equals("pln"))
				{
					/*
					 * "pln" = defines a new plane 
				 	 * 		params[0,1,2] = normal (x, y, z) 
				 	 * 		params[3] = offset 
				  	 * 		params[4] = material index
					 */
					
					if (params.length != 5)
					{
						throw new RayTracerException(
								"Incomapible number of parameters for camera.");

					}
					
					Plane plane = new Plane();
					
					plane.setNormal(new Vector(Double.parseDouble(params[0]), 
											   Double.parseDouble(params[1]), 
											   Double.parseDouble(params[2])));
					plane.setOffset(Double.parseDouble(params[3]));
					plane.setMaterial(Integer.parseInt(params[4]) - 1);
					
					surfaces.add(plane);

					System.out.println(String.format("Parsed plane (line %d)", lineNum));
				}
				else if (code.equals("elp"))
				{
					/*
					 * "elp" = defines a new ellipsoid 
				 	 * 		params[0,1,2] = position of the ellipsoid center (x, y, z) 
				  	 * 		params[3,4,5] = first row of transformation matrix 
				  	 * 		params[6,7,8] = second row of transformation matrix 
				  	 * 		params[9,10,11] = third row of transformation matrix 
				  	 * 		params[12] = material index 
					 */

					if (params.length != 13)
					{
						throw new RayTracerException(
								"Incomapible number of parameters for camera.");

					}
					
					Ellipsoid ellipse = new Ellipsoid();
					
					ellipse.setCenter(new Vector(Double.parseDouble(params[0]), 
												 Double.parseDouble(params[1]), 
												 Double.parseDouble(params[2])));
					ellipse.getMatrix().setRow(0, Double.parseDouble(params[3]), 
							 					  Double.parseDouble(params[4]), 
							 					  Double.parseDouble(params[5]));
					ellipse.getMatrix().setRow(1, Double.parseDouble(params[6]), 
		 					  					  Double.parseDouble(params[7]), 
		 					  					  Double.parseDouble(params[8]));
					ellipse.getMatrix().setRow(2, Double.parseDouble(params[9]), 
												  Double.parseDouble(params[10]), 
												  Double.parseDouble(params[11]));
					ellipse.setMaterial(Integer.parseInt(params[12]) - 1);
					
					surfaces.add(ellipse);
					
					System.out.println(String.format("Parsed ellipsoid (line %d)", lineNum));
				}
				else if (code.equals("lgt"))
				{
					/*
					 * "lgt" = defines a new light 
				 	 * 		params[0,1,2] = position of the light (x, y, z) 
				  	 * 		params[3,4,5] = light color (r, g, b) 
				  	 * 		params[6] = specular intensity 
				  	 * 		params[7] = shadow intensity 
				  	 * 		params[8] = light width / radius (used for soft shadows)
					 */

					if (params.length != 9)
					{
						throw new RayTracerException(
								"Incomapible number of parameters for camera.");

					}

					Light light = new Light();
					
					light.setPosition(new Vector(Double.parseDouble(params[0]), 
												 Double.parseDouble(params[1]), 
												 Double.parseDouble(params[2])));
					light.setColor(new Color(Float.parseFloat(params[3]),
											 Float.parseFloat(params[4]),
											 Float.parseFloat(params[5])));
					light.setSpecularIntensity(Double.parseDouble(params[6]));
					light.setShadowIntensity(Double.parseDouble(params[7]));
					light.setRadius(Double.parseDouble(params[8]));
					
					lights.add(light);
					
					System.out.println(String.format("Parsed light (line %d)", lineNum));
				}
				else
				{
					System.out.println(String.format("ERROR: Did not recognize object: %s (line %d)", code, lineNum));
				}
				
				scene.setLights(lights);
				scene.setMaterials(materials);
				scene.setSurfaces(surfaces);
			}
		}

		// It is recommended that you check here that the scene is valid,
		// for example camera settings and all necessary materials were defined.

		System.out.println("Finished parsing scene file " + sceneFileName);

	}

	/**
	 * Renders the loaded scene and saves it to the specified file location.
	 */
	public void renderScene(String outputFileName)
	{
		long startTime = System.currentTimeMillis();

		// Create a byte array to hold the pixel data:
		byte[] rgbData = new byte[this.imageWidth * this.imageHeight * 3];

		// Put your ray tracing code here!
		//
		// Write pixel color values in RGB format to rgbData:
		// Pixel [x, y] red component is in rgbData[(y * this.imageWidth + x) *
		// 3]
		// green component is in rgbData[(y * this.imageWidth + x) * 3 + 1]
		// blue component is in rgbData[(y * this.imageWidth + x) * 3 + 2]
		//
		// Each of the red, green and blue components should be a byte, i.e.
		// 0-255

		long endTime = System.currentTimeMillis();
		Long renderTime = endTime - startTime;

		// The time is measured for your own conveniece, rendering speed will
		// not affect your score
		// unless it is exceptionally slow (more than a couple of minutes)
		System.out.println("Finished rendering scene in " + renderTime.toString() + " milliseconds.");

		// This is already implemented, and should work without adding any code.
		saveImage(this.imageWidth, rgbData, outputFileName);

		System.out.println("Saved file " + outputFileName);

	}

	// ////////////////////// FUNCTIONS TO SAVE IMAGES IN PNG FORMAT
	// //////////////////////////////////////////

	/*
	 * Saves RGB data as an image in png format to the specified location.
	 */
	public static void saveImage(int width, byte[] rgbData, String fileName)
	{
		try
		{

			BufferedImage image = bytes2RGB(width, rgbData);
			ImageIO.write(image, "png", new File(fileName));

		}
		catch (IOException e)
		{
			System.out.println("ERROR SAVING FILE: " + e.getMessage());
		}

	}

	/*
	 * Producing a BufferedImage that can be saved as png from a byte array of
	 * RGB values.
	 */
	public static BufferedImage bytes2RGB(int width, byte[] buffer)
	{
		int height = buffer.length / width / 3;
		ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
		ColorModel cm = new ComponentColorModel(cs, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
		SampleModel sm = cm.createCompatibleSampleModel(width, height);
		DataBufferByte db = new DataBufferByte(buffer, width * height);
		WritableRaster raster = Raster.createWritableRaster(sm, db, null);
		BufferedImage result = new BufferedImage(cm, raster, false, null);

		return result;
	}

	public static class RayTracerException extends Exception
	{

		public RayTracerException(String msg)
		{
			super(msg);
		}
	}

}
