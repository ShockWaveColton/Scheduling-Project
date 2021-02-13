package engine;

public class Application {
	public Application() {
		Init();
	}
	
	private void Init() {
		new Window(new ObjectManager());
	}
}
