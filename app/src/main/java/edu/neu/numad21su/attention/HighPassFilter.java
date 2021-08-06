package edu.neu.numad21su.attention;

public class HighPassFilter {


    /// <summary>
/// rez amount, from sqrt(2) to ~ 0.1
/// </summary>
    private double resonance;

    private double frequency;
    private int sampleRate;
    private PassType passType;


    public double value;

    private double c, a1, a2, a3, b1, b2;

    /// <summary>
/// Array of input values, latest are in front
/// </summary>
    private double[] inputHistory = new double[2];

    /// <summary>
/// Array of output values, latest are in front
/// </summary>
    private double[] outputHistory = new double[3];

    public HighPassFilter(double frequency, int sampleRate, PassType passType, double resonance) {
        this.resonance = resonance;
        this.frequency = frequency;
        this.sampleRate = sampleRate;
        this.passType = passType;

        switch (passType) {
            case Lowpass:
                c = 1.0f / (double) Math.tan(Math.PI * frequency / sampleRate);
                a1 = 1.0f / (1.0f + resonance * c + c * c);
                a2 = 2f * a1;
                a3 = a1;
                b1 = 2.0f * (1.0f - c * c) * a1;
                b2 = (1.0f - resonance * c + c * c) * a1;
                break;
            case Highpass:
                c = (double) Math.tan(Math.PI * frequency / sampleRate);
                a1 = 1.0f / (1.0f + resonance * c + c * c);
                a2 = -2f * a1;
                a3 = a1;
                b1 = 2.0f * (c * c - 1.0f) * a1;
                b2 = (1.0f - resonance * c + c * c) * a1;
                break;
        }
    }

    public enum PassType {
        Highpass,
        Lowpass,
    }

    public void Update(double newInput) {
        double newOutput = a1 * newInput + a2 * this.inputHistory[0] + a3 * this.inputHistory[1] - b1 * this.outputHistory[0] - b2 * this.outputHistory[1];

        this.inputHistory[1] = this.inputHistory[0];
        this.inputHistory[0] = newInput;

        this.outputHistory[2] = this.outputHistory[1];
        this.outputHistory[1] = this.outputHistory[0];
        this.outputHistory[0] = newOutput;
    }


    public double getValue() {
        return this.outputHistory[0];
    }
}