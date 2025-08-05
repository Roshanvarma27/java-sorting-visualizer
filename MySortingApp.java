import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Random;

public class MySortingApp extends JFrame {
    private int[] array;
    private SortPanel sortPanel;
    private JComboBox<String> algorithmComboBox;
    private JRadioButton ascendingRadioButton;
    private JRadioButton descendingRadioButton;

    public MySortingApp(int[] array) {
        this.array = Arrays.copyOf(array, array.length);
        this.sortPanel = new SortPanel();
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();

        // Combo box for selecting the sorting algorithm
        algorithmComboBox = new JComboBox<>(new String[]{
            "Bubble Sort", "Insertion Sort", "Selection Sort",
            "Quick Sort", "Merge Sort", "Heap Sort"
        });
        algorithmComboBox.setSelectedIndex(0); // Default selection
        controlPanel.add(algorithmComboBox);

        // Radio buttons for selecting the order
        ascendingRadioButton = new JRadioButton("Ascending");
        descendingRadioButton = new JRadioButton("Descending");
        ButtonGroup orderGroup = new ButtonGroup();
        orderGroup.add(ascendingRadioButton);
        orderGroup.add(descendingRadioButton);
        ascendingRadioButton.setSelected(true); // Default selection
        controlPanel.add(ascendingRadioButton);
        controlPanel.add(descendingRadioButton);

        JButton sortButton = new JButton("Sort");
        JButton resetButton = new JButton("Reset");
        sortButton.addActionListener(e -> performSort());
        resetButton.addActionListener(e -> resetArray());
        controlPanel.add(sortButton);
        controlPanel.add(resetButton);

        add(controlPanel, BorderLayout.NORTH);
        add(sortPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void performSort() {
        String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
        boolean isAscending = ascendingRadioButton.isSelected();
        switch (selectedAlgorithm) {
            case "Bubble Sort":
                performBubbleSort(isAscending);
                break;
            case "Insertion Sort":
                performInsertionSort(isAscending);
                break;
            case "Selection Sort":
                performSelectionSort(isAscending);
                break;
            case "Quick Sort":
                performQuickSort(isAscending);
                break;
            case "Merge Sort":
                performMergeSort(isAscending);
                break;
            case "Heap Sort":
                performHeapSort(isAscending);
                break;
        }
    }

    private void performBubbleSort(boolean isAscending) {
        performSort(() -> {
            for (int i = 0; i < array.length - 1; i++) {
                for (int j = 0; j < array.length - 1 - i; j++) {
                    if ((isAscending && array[j] > array[j + 1]) ||
                        (!isAscending && array[j] < array[j + 1])) {
                        swap(j, j + 1);
                    }
                }
            }
        });
    }

    private void performInsertionSort(boolean isAscending) {
        performSort(() -> {
            for (int i = 1; i < array.length; i++) {
                int current = array[i];
                int j = i;
                while (j > 0 && ((isAscending && array[j - 1] > current) ||
                                 (!isAscending && array[j - 1] < current))) {
                    array[j] = array[j - 1];
                    j--;
                }
                array[j] = current;
                sortPanel.repaint();
                sleep();
            }
        });
    }

    private void performSelectionSort(boolean isAscending) {
        performSort(() -> {
            for (int i = 0; i < array.length - 1; i++) {
                int minIndex = i;
                for (int j = i + 1; j < array.length; j++) {
                    if ((isAscending && array[j] < array[minIndex]) ||
                        (!isAscending && array[j] > array[minIndex])) {
                        minIndex = j;
                    }
                }
                if (minIndex != i) {
                    swap(i, minIndex);
                }
            }
        });
    }

    private void performQuickSort(boolean isAscending) {
        performSort(() -> quickSort(0, array.length - 1, isAscending));
    }

    private void quickSort(int low, int high, boolean isAscending) {
        if (low < high) {
            int pivotIndex = partition(low, high, isAscending);
            quickSort(low, pivotIndex - 1, isAscending);
            quickSort(pivotIndex + 1, high, isAscending);
        }
    }

    private int partition(int low, int high, boolean isAscending) {
        int pivot = array[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if ((isAscending && array[j] <= pivot) || (!isAscending && array[j] >= pivot)) {
                i++;
                swap(i, j);
            }
        }
        swap(i + 1, high);
        return i + 1;
    }

    private void performMergeSort(boolean isAscending) {
        performSort(() -> mergeSort(0, array.length - 1, isAscending));
    }

    private void mergeSort(int low, int high, boolean isAscending) {
        if (low < high) {
            int mid = (low + high) / 2;
            mergeSort(low, mid, isAscending);
            mergeSort(mid + 1, high, isAscending);
            merge(low, mid, high, isAscending);
        }
    }

    private void merge(int low, int mid, int high, boolean isAscending) {
        int[] temp = Arrays.copyOf(array, array.length);
        Color[] tempColors = Arrays.copyOf(sortPanel.barColors, sortPanel.barColors.length);
        int i = low, j = mid + 1, k = low;
        while (i <= mid && j <= high) {
            if ((isAscending && temp[i] <= temp[j]) || (!isAscending && temp[i] >= temp[j])) {
                array[k] = temp[i];
                sortPanel.barColors[k] = tempColors[i];
                i++;
            } else {
                array[k] = temp[j];
                sortPanel.barColors[k] = tempColors[j];
                j++;
            }
            k++;
            sortPanel.repaint();
            sleep();
        }
        while (i <= mid) {
            array[k] = temp[i];
            sortPanel.barColors[k] = tempColors[i];
            k++;
            i++;
        }
        while (j <= high) {
            array[k] = temp[j];
            sortPanel.barColors[k] = tempColors[j];
            k++;
            j++;
        }
        sortPanel.repaint();
    }

    private void performHeapSort(boolean isAscending) {
        performSort(() -> {
            int n = array.length;
            for (int i = n / 2 - 1; i >= 0; i--) {
                heapify(n, i, isAscending);
            }
            for (int i = n - 1; i > 0; i--) {
                swap(0, i);
                heapify(i, 0, isAscending);
            }
        });
    }

    private void heapify(int n, int i, boolean isAscending) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        if (left < n && ((isAscending && array[left] > array[largest]) || (!isAscending && array[left] < array[largest]))) {
            largest = left;
        }
        if (right < n && ((isAscending && array[right] > array[largest]) || (!isAscending && array[right] < array[largest]))) {
            largest = right;
        }
        if (largest != i) {
            swap(i, largest);
            heapify(n, largest, isAscending);
        }
    }

    private void performSort(Runnable sortingAlgorithm) {
        new Thread(() -> {
            sortingAlgorithm.run();
            Color[] sortedColors = new Color[array.length];
            Arrays.fill(sortedColors, Color.GREEN); // Sorted color
            sortPanel.updateArray(array, sortedColors);
        }).start();
    }

    private void resetArray() {
        String input = JOptionPane.showInputDialog(this, "Enter numbers separated by commas:");
        try {
            array = Arrays.stream(input.split(","))
                          .map(String::trim)
                          .mapToInt(Integer::parseInt)
                          .toArray();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Using random array.");
            array = generateStartingArray(20, 0, 100);
        }
        Color[] resetColors = new Color[array.length];
        Arrays.fill(resetColors, Color.RED); // Reset color
        sortPanel.updateArray(array, resetColors);
    }

    private void swap(int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        Color tempColor = sortPanel.barColors[i];
        sortPanel.barColors[i] = sortPanel.barColors[j];
        sortPanel.barColors[j] = tempColor;
        sortPanel.repaint();
        sleep();
    }

    private void sleep() {
        try {
            Thread.sleep(100); // Delay for visualization
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class SortPanel extends JPanel {
        private static final int BAR_WIDTH = 20;
        private static final int BAR_SPACING = 5;
        private Color[] barColors;

        private SortPanel() {
            this.barColors = new Color[array.length];
            Arrays.fill(barColors, Color.RED);
        }

        private void updateArray(int[] newArray, Color[] newColors) {
            array = Arrays.copyOf(newArray, newArray.length);
            barColors = Arrays.copyOf(newColors, newColors.length);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int x = 0;
            for (int i = 0; i < array.length; i++) {
                int barHeight = array[i] * 5;
                int y = getHeight() - barHeight;
                g2d.setColor(barColors[i]);
                g2d.fillRect(x, y, BAR_WIDTH, barHeight);

                // Draw value above bar
                g2d.setColor(Color.BLACK);
                g2d.drawString(String.valueOf(array[i]), x + BAR_WIDTH / 4, y - 5);

                x += BAR_WIDTH + BAR_SPACING;
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension((BAR_WIDTH + BAR_SPACING) * array.length, 400);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String input = JOptionPane.showInputDialog("Enter numbers separated by commas:");
            int[] array;
            try {
                array = Arrays.stream(input.split(","))
                              .map(String::trim)
                              .mapToInt(Integer::parseInt)
                              .toArray();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Using default array.");
                array = generateStartingArray(20, 0, 100);
            }
            new MySortingApp(array);
        });
    }

    private static int[] generateStartingArray(int n, int minVal, int maxVal) {
        int[] array = new int[n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            array[i] = random.nextInt(maxVal - minVal + 1) + minVal;
        }
        return array;
    }
}
