package  com.xzy.forestSystem.gogisapi.Tools;

import java.text.DecimalFormat;

/* compiled from: CalXuJiDialog */
class FastComputation {
    private static final FastComputation instance = new FastComputation();
    protected double Cm1;
    protected double Cm2;

    /* renamed from: D */
    protected double f546D;

    /* renamed from: G1 */
    protected double f547G1;

    /* renamed from: G2 */
    protected double f548G2;

    /* renamed from: G3 */
    protected double f549G3;

    /* renamed from: H */
    protected double f550H;

    /* renamed from: S1 */
    protected double f551S1;

    /* renamed from: S2 */
    protected double f552S2;

    /* renamed from: S3 */
    protected double f553S3;

    /* renamed from: T */
    protected int f554T;
    protected double Vm1;
    protected double Vm2;
    protected double Vm3;

    /* renamed from: W */
    protected int f555W;

    /* renamed from: df */
    public DecimalFormat f556df = new DecimalFormat("#");
    public DecimalFormat df1 = new DecimalFormat("#.0");

    private FastComputation() {
    }

    public static FastComputation getInstance() {
        return instance;
    }

    public String[] execute(double h, double d, double f, int w, int t) {
        this.f546D = d;
        this.f550H = h;
        this.f555W = w;
        this.f554T = t;
        switch (t) {
            case 0:
                this.f551S1 = f;
                break;
            case 1:
                this.Vm1 = f;
                break;
            case 2:
                this.f547G1 = f;
                break;
        }
        switch (this.f554T) {
            case 0:
                return MGSMD();
            case 1:
                return MGMMXJ();
            case 2:
                return JGRCDM();
            default:
                return null;
        }
    }

    /* access modifiers changed from: protected */
    public String[] MGSMD() {
        this.f553S3 = this.f551S1 / 10.0d;
        this.Vm3 = this.f553S3 * queryData(1.0d);
        this.Cm2 = (this.f551S1 / 10.0d) * queryData(0.0d);
        this.f549G3 = ((this.Vm3 * 15.0d) / m2XG()) / (this.f550H + 3.0d);
        return toStingArray(new double[]{this.f553S3, this.Cm2, this.Vm3, this.f549G3});
    }

    /* access modifiers changed from: protected */
    public String[] MGMMXJ() {
        this.f553S3 = this.Vm1 / queryData(1.0d);
        this.Cm2 = (queryData(0.0d) * this.Vm1) / queryData(1.0d);
        this.Vm3 = this.Vm1;
        this.f549G3 = ((this.Vm3 * 15.0d) / m2XG()) / (this.f550H + 3.0d);
        return toStingArray(new double[]{this.f553S3, this.Cm2, this.Vm3, this.f549G3});
    }

    /* access modifiers changed from: protected */
    public String[] JGRCDM() {
        this.Vm3 = ((this.f547G1 * m2XG()) * (this.f550H + 3.0d)) / 15.0d;
        this.f553S3 = this.Vm3 / queryData(1.0d);
        this.Cm2 = this.f553S3 * queryData(0.0d);
        this.f549G3 = this.f547G1;
        return toStingArray(new double[]{this.f553S3, this.Cm2, this.Vm3, this.f549G3});
    }

    /* access modifiers changed from: protected */
    /* renamed from: XG */
    public double m2XG() {
        switch (this.f555W) {
            case 0:
            default:
                return 0.39d;
            case 1:
                return 0.42d;
            case 2:
                return 0.38d;
        }
    }

    /* access modifiers changed from: protected */
    public String[] toStingArray(double[] a) {
        String[] s = new String[4];
        int i = 0;
        for (double b : a) {
            if (i == 1) {
                s[i] = String.valueOf(this.f556df.format(b));
            } else {
                s[i] = String.valueOf(this.df1.format(b));
            }
            i++;
        }
        return s;
    }

    /* access modifiers changed from: protected */
    public double queryData(double type) {
        if (type == 0.0d) {
            int a = ((int) this.f550H) - 4;
            int b = (((int) (this.f546D - 5.0d)) * 3) + this.f555W;
            if (((double) ((int) this.f546D)) < RefData.SGtoXJ[a][0] || ((double) ((int) this.f546D)) > RefData.SGtoXJ[a][1]) {
                return 0.0d;
            }
            return RefData.BZLFMMZS[a][b];
        }
        return RefData.SMD1MMXJ[((int) this.f550H) - 4][this.f555W];
    }
}
