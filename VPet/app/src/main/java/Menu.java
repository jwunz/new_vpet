import java.util.List;

/**
 * Created by bwoods on 5/14/2017.
 */

public class Menu {

    private int width = 0;
    private int height = 0;
    private String img = "";
    private int y = 0;
    private boolean isClosable;
    private List<UIElement> UIElements;

    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public String getImg(){
        return img;
    }
    public int y(){
        return y;
    }
    public boolean getIsClosable(){
        return isClosable;
    }
    public boolean displayElements(){
        return true;
    }
    public Menu(int width, int height, String img, int y, boolean isClosable, List<UIElement> UIElements){
        this.width = width;
        this.height = height;
        this.img = img;
        this.y = y;
        this.isClosable = isClosable;
        this.UIElements = UIElements;
    }
}
