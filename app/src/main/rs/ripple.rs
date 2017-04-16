#pragma version(1)
#pragma rs java_package_name(com.example.uberv.a8_1addingnativebitswithjni)

float centerX;
float centerY;
float minRadius;
//Amplitude control of the wave peaks
float scalar;
//Wave Dampener, larger values damp out the ripples sooner
float damper;
//Sine frequency, larger values show more ripples
float frequency;

// TIP
// if your algorithm requires analyzing neighboring data in the allocation, you can set the input
// allocation as a script global as well. this allows you to read any item in the allocation you wish,
// via rs_GetElementAt_type(), to do your computations.

// The root() function is the main kernel, which will execute over each pixel
// in the image bitmap. The ripple effect will be applied by determining
// how far away the input pixel is from the center point, and calculation
// whether it should be lightened or darkened based on a decaying sine wavefunction
void root(const uchar4* v_in, uchar4* v_out, const void* usrData, uint32_t x, uint32_t y)
{
    // uchar4 - unsigned character vector with size 4

    //Compute distance from the center
    float dx = x - centerX;
    float dy = y - centerY;
    float radius = sqrt(dx*dx + dy*dy);

    if (radius < minRadius)
    {
        //Use the original pixel
        *v_out = *v_in;
    }
    else
    {
        // convert the input pixel into an ARGB float vector for future math
        float4 f4 = rsUnpackColor8888(*v_in);

        float shiftedRadius = radius - minRadius;

        //Determine sine function multiplier based on distance
        // (y=e-ktsin(wt), where t represents the distance from center)
        float multiplier = (scalar * exp(-shiftedRadius * damper) * -sin(shiftedRadius * frequency)) + 1;
        //Lighten or darken pixel, within min/max range defined
        float3 transformed = f4.rgb * multiplier;
        *v_out = rsPackColorTo8888(transformed);
    }
}