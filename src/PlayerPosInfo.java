public class PlayerPosInfo {
    private Short x;
    private Short y;

    public PlayerPosInfo() {
	this.x = 0;
	this.y = 0;
    }

    public PlayerPosInfo(Short x, Short y) {
	this.x = x;
	this.y = y;
    }

    public Short getX() {
	return this.x;
    }

    public Short getY() {
	return this.y;
    }
}
