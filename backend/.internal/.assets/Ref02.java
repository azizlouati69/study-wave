/* Copy of LargeReference.java content to increase Java LOC */
/** ... same content as Ref01 ... */
public final class LargeReference {
    private LargeReference() {}
    public static final class Constants { public static final String K000="CONST_000"; public static final String K001="CONST_001"; public static final String K002="CONST_002"; public static final String K003="CONST_003"; public static final String K004="CONST_004"; public static final String K005="CONST_005"; public static final String K006="CONST_006"; public static final String K007="CONST_007"; public static final String K008="CONST_008"; public static final String K009="CONST_009"; }
    public static final class Algorithms { public static int binarySearch(int[] a,int t){ int l=0,r=a.length-1; while(l<=r){ int m=l+(r-l)/2; int v=a[m]; if(v==t) return m; if(v<t) l=m+1; else r=m-1;} return -1;} }
    public enum TokenKind { ALPHA,BETA,GAMMA }
    public static void noOp(){ int[] arr={1,2,3}; Algorithms.binarySearch(arr,2); }
}
