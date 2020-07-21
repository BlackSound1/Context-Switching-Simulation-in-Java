public class CPU  {
    private Process process;
    private int CPUID;
    private CPUState state;

    public CPU() {
        process = null;
        CPUID = -1;
        state = CPUState.BUSY;
    }

    public CPU(Process process) {
        this.process = process;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public int getCPUID() {
        return CPUID;
    }

    public void setCPUID(int CPUID) {
        this.CPUID = CPUID;
    }

    public CPUState getState() {
        return state;
    }

    public void setState(CPUState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "CPU{" +
                "process=" + process +
                '}';
    }

}
