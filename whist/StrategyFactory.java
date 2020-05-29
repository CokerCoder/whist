public class StrategyFactory {
    private static StrategyFactory factory;
    public static StrategyFactory getInstance(){
        if(factory==null) factory = new StrategyFactory();
        return factory;
    }
    private StrategyFactory(){

    }
    public IPlayingStrategy getSmartStrategy() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String className = System.getProperty("SmartStrategy.class.name");
        return (IPlayingStrategy)Class.forName(className).newInstance();
    }
    public IPlayingStrategy getRandomStrategy() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String className = System.getProperty("RandomStrategy.class.name");
        return (IPlayingStrategy)Class.forName(className).newInstance();
    }
    public IPlayingStrategy getLegalStrategy() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String className = System.getProperty("LegalStrategy.class.name");
        return (IPlayingStrategy)Class.forName(className).newInstance();
    }
}
