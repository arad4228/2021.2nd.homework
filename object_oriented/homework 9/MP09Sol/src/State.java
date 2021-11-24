public interface State {
    default void processNumber(int num) {
        System.out.println("processNumber: Invalid operation");
    }

    default void processOperator(char op) {
        System.out.println("processOperator: Invalid operation");
    }
}
