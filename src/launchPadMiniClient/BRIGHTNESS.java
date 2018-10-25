package launchPadMiniClient;

public enum BRIGHTNESS {
    LOW(0x7D),
    MEDIUM(0x7E),
    FULL(0x7F);

    private final byte code;
    protected byte code() { return code; }

    BRIGHTNESS(int code)
    {
        this.code = (byte)code;
    }

    public BRIGHTNESS getRandom() {
        return Extensions.randomEnum(BRIGHTNESS.class);
    }

}

