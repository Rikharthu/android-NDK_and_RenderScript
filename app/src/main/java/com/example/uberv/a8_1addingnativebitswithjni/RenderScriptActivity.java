package com.example.uberv.a8_1addingnativebitswithjni;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.renderscript.ScriptIntrinsicColorMatrix;
import android.renderscript.ScriptIntrinsicConvolve3x3;
import android.widget.ImageView;

/*
RenderScript kernels are functions invoked across a large input data set, known as an allocation. The
RenderScript engine invokes a given kernel over every item in an input allocation to produce a result in a
second output allocation. As an example, if a kernel is designed to process image data, the input allocation
would be the original image bitmap to process, and the output allocation would contain the processed
image bitmap data.
 */
public class RenderScriptActivity extends AppCompatActivity {

    private enum ConvolutionFilter {
        SHARPEN, LIGHTEN, DARKEN, EDGE_DETECT, EMBOSS
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_render_script);

        // Create the source data, and a desitonation for the filtered results
        Bitmap inBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cat);
        Bitmap outBitmap = inBitmap.copy(inBitmap.getConfig(), true);
        // Show the normal image
        setImageInView(outBitmap, R.id.image_normal);

        // Create RenderScript context
        final RenderScript rs = RenderScript.create(this);
        // Create allocations for input and output data
        final Allocation input = Allocation.createFromBitmap(rs, inBitmap,
                Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT); // the allocation will be bound to and accessed by scripts
        final Allocation output = Allocation.createTyped(rs, input.getType());

        // Run blur script
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur
                .create(rs, Element.U8_4(rs));
        // Element represents one item within an Allocation
        // We choose U8_4 becuase our bitmap has ARGB pixel data,
        // so each element (that is, pixel) is 4 unsigned bytes in size
        script.setRadius(10f);
        script.setInput(input);
        // apply given script for each element in a given Allocation
        script.forEach(output);
        // copy values from Allocation to bitmap
        output.copyTo(outBitmap);
        setImageInView(outBitmap, R.id.image_blurred);

        // Run grayscale script
        final ScriptIntrinsicColorMatrix scriptColor = ScriptIntrinsicColorMatrix
                .create(rs, Element.U8_4(rs));
        scriptColor.setGreyscale();
        scriptColor.forEach(input, output);
        output.copyTo(outBitmap);
        setImageInView(outBitmap, R.id.image_greyscale);

        // Run sharpen script
        ScriptIntrinsicConvolve3x3 scriptC = ScriptIntrinsicConvolve3x3
                .create(rs, Element.U8_4(rs));
        // provide a custom filter kernel (like in matlab)
        scriptC.setCoefficients(getCoefficients(ConvolutionFilter.SHARPEN));
        scriptC.setInput(input);
        scriptC.forEach(output);
        output.copyTo(outBitmap);
        setImageInView(outBitmap, R.id.image_sharpen);

        //Run edge detect script
        scriptC = ScriptIntrinsicConvolve3x3.create(rs, Element.U8_4(rs));
        scriptC.setCoefficients(getCoefficients(ConvolutionFilter.EDGE_DETECT));
        scriptC.setInput(input);
        scriptC.forEach(output);
        output.copyTo(outBitmap);
        setImageInView(outBitmap, R.id.image_edge);

        //Run emboss script
        scriptC = ScriptIntrinsicConvolve3x3.create(rs, Element.U8_4(rs));
        scriptC.setCoefficients(getCoefficients(ConvolutionFilter.EMBOSS));
        scriptC.setInput(input);
        scriptC.forEach(output);
        output.copyTo(outBitmap);
        setImageInView(outBitmap, R.id.image_emboss);

        //Tear down the RenderScript context
        rs.destroy();
    }

    private void setImageInView(Bitmap bm, int viewId) {
        ImageView normalImage = (ImageView) findViewById(viewId);
        normalImage.setImageBitmap(bm.copy(bm.getConfig(), false));
    }

    /**
     * Helper to obtain matrix coefficients for each type of
     * convolution image filter.
     */
    private float[] getCoefficients(ConvolutionFilter filter) {
        switch (filter) {
            case SHARPEN:
                return new float[]{
                        0f, -1f, 0f,
                        -1f, 5f, -1f,
                        0f, -1f, 0f
                };
            case LIGHTEN:
                return new float[]{
                        0f, 0f, 0f,
                        0f, 1.5f, 0f,
                        0f, 0f, 0f
                };
            case DARKEN:
                return new float[]{
                        0f, 0f, 0f,
                        0f, 0.5f, 0f,
                        0f, 0f, 0f
                };
            case EDGE_DETECT:
                return new float[]{
                        -1f, 0f, 1f,
                        -2f, 0f, 2f,
                        -1f, 0f, 1f
                };
            case EMBOSS:
                return new float[]{
                        -2f, -1f, 0f,
                        -1f, 1f, 1f,
                        0f, 1f, 2f
                };
            default:
                return null;
        }
    }
}
