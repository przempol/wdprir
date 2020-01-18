import pandas as pd
import matplotlib.pyplot as plt


if __name__ == '__main__':
    print('xD')
    f3 = 'task3.csv'
    f4 = 'task4.csv'
    f5 = 'task5.csv'
    f6 = 'task6.csv'
    f7 = 'task7.csv'
    df3: pd.DataFrame = pd.read_csv(f3)
    df4: pd.DataFrame = pd.read_csv(f4)
    df5: pd.DataFrame = pd.read_csv(f5)
    df6: pd.DataFrame = pd.read_csv(f6)
    df7: pd.DataFrame = pd.read_csv(f7)

    x3 = df3[df3.columns[[0]]]
    y3 = df3[df3.columns[[1]]]
    y4 = df4[df4.columns[[1]]]
    y5 = df5[df5.columns[[1]]]
    y6 = df6[df6.columns[[1]]]
    fig1 = plt.figure()
    ax = plt.subplot(111)
    ax.plot(x3, y3, 'r--', label='sekwencyjnie')
    ax.plot(x3, y4, 'b--', label='równolegle prosto')
    ax.plot(x3, y5, 'g--', label='równolegle ze stałą pulą wątków')
    ax.plot(x3, y6, 'y--', label='równolegle ścigające się')
    plt.title('Porównanie czasów sumowania tablicy')
    ax.set_xlabel('długość tablicy')
    ax.set_ylabel('czas (ns)')
    ax.legend()

    fig2 = plt.figure()
    x7 = df7[df7.columns[[0]]]
    y7 = df7[df7.columns[[1]]]
    x7 = x7[:8]
    y7 = y7[:8]
    # print(x7.min())
    ax = plt.subplot(111)
    ax.plot(x7, y7, 'r--', label='równolegle ścigajace się sekwencjami')
    ax.hlines(y3.iloc[-1], x7.min(), x7.max(), colors='b', linestyles='solid', label='sekwencyjnie')
    ax.hlines(y4.iloc[-1], x7.min(), x7.max(), colors='m', linestyles='solid', label='równolegle prosto')
    plt.title('Zależność czasu od długości sekwencji')
    ax.set_xlabel('długość sekwencji')
    ax.set_ylabel('czas (ns)')
    ax.legend()
    plt.show()
