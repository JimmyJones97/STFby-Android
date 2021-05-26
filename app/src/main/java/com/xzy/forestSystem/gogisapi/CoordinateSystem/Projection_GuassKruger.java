package  com.xzy.forestSystem.gogisapi.CoordinateSystem;

import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;

public class Projection_GuassKruger extends ProjectionCoordinateSystem {
    @Override //  com.xzy.forestSystem.gogisapi.CoordinateSystem.ProjectionCoordinateSystem,  com.xzy.forestSystem.gogisapi.CoordinateSystem.AbstractC0383CoordinateSystem
    public Coordinate ConvertXYToBL(double X, double Y) {
        double B = 0.0d;
        double L = 0.0d;
        if (!(X == 0.0d || Y == 0.0d)) {
            double num = Math.toRadians((double) this._CenterMeridian);
            double num3 = this.f462_A;
            double num4 = this.f463_B;
            double x = Math.sqrt(1.0d - ((num4 / num3) * (num4 / num3)));
            double num7 = Math.sqrt(((num3 / num4) * (num3 / num4)) - 1.0d);
            double num10 = this._Easting;
            double num13 = (1.0d - (num4 / num3)) / (1.0d + (num4 / num3));
            double num14 = ((Y - 0.0d) / ((double) 1)) / ((((1.0d - ((x * x) / 4.0d)) - ((3.0d * Math.pow(x, 4.0d)) / 64.0d)) - ((5.0d * Math.pow(x, 6.0d)) / 256.0d)) * num3);
            double a = ((((3.0d * num13) / 2.0d) - ((((27.0d * num13) * num13) * num13) / 32.0d)) * Math.sin(2.0d * num14)) + num14 + (((((21.0d * num13) * num13) / 16.0d) - ((55.0d * Math.pow(num13, 4.0d)) / 32.0d)) * Math.sin(4.0d * num14)) + (((((151.0d * num13) * num13) * num13) / 96.0d) * Math.sin(6.0d * num14)) + (((1097.0d * Math.pow(num13, 4.0d)) / 512.0d) * Math.sin(8.0d * num14));
            double num16 = num3 / Math.sqrt(1.0d - (((x * x) * Math.sin(a)) * Math.sin(a)));
            double num17 = (X - num10) / (((double) 1) * num16);
            double num18 = Math.tan(a) * Math.tan(a);
            double num19 = num7 * num7 * Math.cos(a) * Math.cos(a);
            B = Math.toDegrees(a - (((Math.tan(a) * num16) / (((1.0d - (x * x)) * num3) / Math.sqrt(Math.pow(1.0d - (((x * x) * Math.sin(a)) * Math.sin(a)), 3.0d)))) * ((((num17 * num17) / 2.0d) - (((((5.0d + (3.0d * num18)) + num19) - ((9.0d * num18) * num19)) * Math.pow(num17, 4.0d)) / 24.0d)) + ((((61.0d + (90.0d * num18)) + ((45.0d * num18) * num18)) * Math.pow(num17, 6.0d)) / 720.0d))));
            L = Math.toDegrees(num + ((1.0d / Math.cos(a)) * ((num17 - ((((1.0d + (2.0d * num18)) + num19) * Math.pow(num17, 3.0d)) / 6.0d)) + ((((((5.0d + (28.0d * num18)) + (6.0d * num19)) + ((8.0d * num18) * num19)) + ((24.0d * num18) * num18)) * Math.pow(num17, 5.0d)) / 120.0d))));
        }
        return new Coordinate(X, Y, L, B);
    }

    @Override //  com.xzy.forestSystem.gogisapi.CoordinateSystem.ProjectionCoordinateSystem,  com.xzy.forestSystem.gogisapi.CoordinateSystem.AbstractC0383CoordinateSystem
    public Coordinate ConvertBLToXY(double longitude, double latitude) {
        double X = 0.0d;
        double Y = 0.0d;
        if (!(longitude == 0.0d || latitude == 0.0d)) {
            double num = Math.toRadians((double) this._CenterMeridian);
            double num3 = this._Easting;
            double x = Math.sqrt(1.0d - ((this.f463_B / this.f462_A) * (this.f463_B / this.f462_A)));
            double num9 = Math.sqrt(((this.f462_A / this.f463_B) * (this.f462_A / this.f463_B)) - 1.0d);
            double B = Math.toRadians(latitude);
            double L = Math.toRadians(longitude);
            double num12 = Math.tan(B) * Math.tan(B);
            double num13 = num9 * num9 * Math.cos(B) * Math.cos(B);
            double num14 = (L - num) * Math.cos(B);
            double num15 = this.f462_A * (((((((1.0d - ((x * x) / 4.0d)) - ((3.0d * Math.pow(x, 4.0d)) / 64.0d)) - ((5.0d * Math.pow(x, 6.0d)) / 256.0d)) * B) - ((((((3.0d * x) * x) / 8.0d) + ((3.0d * Math.pow(x, 4.0d)) / 32.0d)) + ((45.0d * Math.pow(x, 6.0d)) / 1024.0d)) * Math.sin(2.0d * B))) + ((((15.0d * Math.pow(x, 4.0d)) / 256.0d) + ((45.0d * Math.pow(x, 6.0d)) / 1024.0d)) * Math.sin(4.0d * B))) - (((35.0d * Math.pow(x, 6.0d)) / 3072.0d) * Math.sin(6.0d * B)));
            double num16 = this.f462_A / Math.sqrt(1.0d - ((x * x) * (Math.sin(B) * Math.sin(B))));
            Y = 0.0d + (((double) 1) * ((Math.tan(B) * num16 * (((num14 * num14) / 2.0d) + (((((5.0d - num12) + (9.0d * num13)) + ((4.0d * num13) * num13)) * Math.pow(num14, 4.0d)) / 24.0d))) + num15 + ((((((61.0d - (58.0d * num12)) + (num12 * num12)) + (270.0d * num13)) - ((330.0d * num12) * num13)) * Math.pow(num14, 6.0d)) / 720.0d)));
            X = num3 + (((double) 1) * num16 * (((((1.0d - num12) + num13) * Math.pow(num14, 3.0d)) / 6.0d) + num14 + ((((((5.0d - (18.0d * num12)) + (num12 * num12)) + (14.0d * num13)) - ((58.0d * num12) * num13)) * Math.pow(num14, 5.0d)) / 120.0d)));
        }
        return new Coordinate(X, Y, longitude, latitude);
    }
}
