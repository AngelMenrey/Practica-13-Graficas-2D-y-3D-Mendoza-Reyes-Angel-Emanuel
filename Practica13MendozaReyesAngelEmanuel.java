import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Material;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TexCoordGeneration;
import javax.media.j3d.Text3D;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.Alpha;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

public class Practica13MendozaReyesAngelEmanuel extends JPanel {

    public Practica13MendozaReyesAngelEmanuel() {
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas3d = new Canvas3D(config);
        setLayout(new BorderLayout());
        add(canvas3d);
        SimpleUniverse universo = new SimpleUniverse(canvas3d);
        universo.getViewingPlatform().setNominalViewingTransform();

        BranchGroup escena = crearGrafoEscena();
        escena.compile();

        universo.addBranchGraph(escena);
    }

    public BranchGroup crearGrafoEscena() {
        BranchGroup objetoRaiz = new BranchGroup();

        TextureLoader fondoLoader = new TextureLoader("fondo.jpg", this);
        ImageComponent2D imagenFondo = fondoLoader.getImage();
        Background fondo = new Background();
        fondo.setImage(imagenFondo);
        fondo.setImageScaleMode(Background.SCALE_FIT_ALL);
        fondo.setApplicationBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
        objetoRaiz.addChild(fondo);

        Color3f lightColor = new Color3f(1.0f, 1.0f, 1.0f);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);

        AmbientLight ambientLight = new AmbientLight(new Color3f(0.2f, 0.2f, 0.2f));
        ambientLight.setInfluencingBounds(bounds);
        objetoRaiz.addChild(ambientLight);

        Vector3f lightDirection = new Vector3f(-1.0f, -1.0f, -4.0f);
        DirectionalLight directionalLight = new DirectionalLight(lightColor, lightDirection);
        directionalLight.setInfluencingBounds(bounds);
        objetoRaiz.addChild(directionalLight);

        Transform3D transformCentral = new Transform3D();
        transformCentral.setTranslation(new Vector3f(0.0f, -0.3f, 0.0f));
        TransformGroup tgCentral = new TransformGroup(transformCentral);
        tgCentral.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Sphere esferaCentral = new Sphere(0.3f, crearApariencia("sol.jpg", true));
        tgCentral.addChild(esferaCentral);
        objetoRaiz.addChild(tgCentral);

        Transform3D yAxisSol = new Transform3D();
        Alpha rotationAlphaSol = new Alpha(-1, 4000);
        RotationInterpolator rotatorSol = new RotationInterpolator(rotationAlphaSol, tgCentral, yAxisSol, 0.0f, (float) Math.PI * 2.0f);
        BoundingSphere boundsSol = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        rotatorSol.setSchedulingBounds(boundsSol);
        tgCentral.addChild(rotatorSol);

        TransformGroup tgRotacion = new TransformGroup();
        tgRotacion.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objetoRaiz.addChild(tgRotacion);

        Transform3D transformDerecha = new Transform3D();
        transformDerecha.setTranslation(new Vector3f(0.6f, 0.0f, 0.0f));
        TransformGroup tgDerecha = new TransformGroup(transformDerecha);
        tgDerecha.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Sphere esferaDerecha = new Sphere(0.2f, crearApariencia("tierra.jpg", false)); 
        tgDerecha.addChild(esferaDerecha);
        tgRotacion.addChild(tgDerecha);

        Transform3D transformIzquierda = new Transform3D();
        transformIzquierda.setTranslation(new Vector3f(-0.6f, 0.0f, 0.0f));
        TransformGroup tgIzquierda = new TransformGroup(transformIzquierda);
        tgIzquierda.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Sphere esferaIzquierda = new Sphere(0.2f, crearApariencia("marte.jpg", false));
        tgIzquierda.addChild(esferaIzquierda);
        tgRotacion.addChild(tgIzquierda);

        Transform3D yAxis = new Transform3D();
        Alpha rotationAlpha = new Alpha(-1, 10000);
        RotationInterpolator rotator = new RotationInterpolator(rotationAlpha, tgRotacion, yAxis, 0.0f, (float) Math.PI * 2.0f);
        rotator.setSchedulingBounds(bounds);
        tgRotacion.addChild(rotator);

        Appearance aparienciaTexto = new Appearance();
        ColoringAttributes colorTexto = new ColoringAttributes();
        colorTexto.setColor(new Color3f(Color.WHITE));
        colorTexto.setShadeModel(ColoringAttributes.NICEST);
        aparienciaTexto.setColoringAttributes(colorTexto);

        Font3D fuente3d = new Font3D(new Font("Courier", Font.PLAIN, 1), new FontExtrusion());
        Text3D texto3d = new Text3D(fuente3d, "Universo", new Point3f(0f, 1.2f, -2.0f)); 
        texto3d.setAlignment(Text3D.ALIGN_CENTER);
        Shape3D forma3d = new Shape3D();
        forma3d.setGeometry(texto3d);
        forma3d.setAppearance(aparienciaTexto);

        Transform3D escala3d = new Transform3D();
        escala3d.setScale(0.3f);
        TransformGroup escala = new TransformGroup(escala3d);

        TransformGroup tgTexto = new TransformGroup();
        tgTexto.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tgTexto.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objetoRaiz.addChild(tgTexto);

        tgTexto.addChild(escala);
        escala.addChild(forma3d);

        Transform3D yAxisTexto = new Transform3D();
        Alpha rotationAlphaTexto = new Alpha(-1, 4000);
        RotationInterpolator rotatorTexto = new RotationInterpolator(rotationAlphaTexto, tgTexto, yAxisTexto, 0.0f, (float) Math.PI * 2.0f);
        rotatorTexto.setSchedulingBounds(bounds);
        tgTexto.addChild(rotatorTexto);

        return objetoRaiz;
    }

    Appearance crearApariencia(String texturaArchivo, boolean esSol) {
        Appearance apariencia = new Appearance();

        TexCoordGeneration texCoord = new TexCoordGeneration(
            TexCoordGeneration.OBJECT_LINEAR,
            TexCoordGeneration.TEXTURE_COORDINATE_2
        );
        apariencia.setTexCoordGeneration(texCoord);

        TextureLoader loader = new TextureLoader(texturaArchivo, this);
        ImageComponent2D imagen = loader.getImage();
        if (imagen == null) {
            System.err.println("Error loading image: " + texturaArchivo);
            return apariencia;
        }

        Texture2D textura = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
            imagen.getWidth(), imagen.getHeight());
        textura.setImage(0, imagen);
        textura.setEnable(true);

        textura.setMagFilter(Texture.BASE_LEVEL_LINEAR);
        textura.setMinFilter(Texture.BASE_LEVEL_LINEAR);

        apariencia.setTexture(textura);
        apariencia.setTextureAttributes(new TextureAttributes());

        Material material = new Material();
        if (esSol) {
            material.setEmissiveColor(new Color3f(1.0f, 1.0f, 0.0f));
        } else {
            material.setAmbientColor(new Color3f(Color.DARK_GRAY));
            material.setDiffuseColor(new Color3f(Color.WHITE));
            material.setSpecularColor(new Color3f(Color.WHITE));
            material.setShininess(20.0f);
        }
        apariencia.setMaterial(material);

        return apariencia;
    }

    public static void main(String[] args) {
        System.setProperty("sun.awt.noerasebackground", "true");
        JFrame ventana = new JFrame("Practica 13 - Mendoza Reyes Angel Emanuel");
        Practica13MendozaReyesAngelEmanuel panel = new Practica13MendozaReyesAngelEmanuel();
        ventana.add(panel);
        ventana.setSize(900, 700);
        ventana.setVisible(true);
        ventana.setLocationRelativeTo(null);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}