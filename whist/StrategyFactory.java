public class StrategyFactory {
    private static StrategyFactory factory;
    public static StrategyFactory getInstance(){
        if(factory==null) factory = new StrategyFactory();
        return factory;
    }
    private StrategyFactory(){

    }
    public IPlayingStrategy getSmartStrategy() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String className = SmartStrategy.class.getName();
        return (IPlayingStrategy)Class.forName(className).newInstance();
    }
    public IPlayingStrategy getRandomStrategy() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String className = RandomStrategy.class.getName();
        return (IPlayingStrategy)Class.forName(className).newInstance();
    }
    public IPlayingStrategy getLegalStrategy() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String className = LegalStrategy.class.getName();
        return (IPlayingStrategy)Class.forName(className).newInstance();
    }
    public IPlayingStrategy getHumanStrategy() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String className = HumanStrategy.class.getName();
        return (IPlayingStrategy)Class.forName(className).newInstance();
    }
}
