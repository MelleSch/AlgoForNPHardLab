from matplotlib import pyplot as plt
import numpy as np
from math import comb
from scipy.stats import nbinom

# x = np.arange(1,11) 
# y = 2 * x + 5 
# plt.title("Matplotlib demo") 
# plt.xlabel("x axis caption") 
# plt.ylabel("y axis caption") 
# plt.plot(x,y) 
# plt.show()

def binomial(p, n):
    values = list(range(n+1))
    dist = [comb(n, i) * p ** i * (1-p) ** (n - i) for i in range(n + 1)]

    plt.title("Binomial distribution") 
    plt.xlabel("Number of samples") 
    plt.ylabel("Probability of successes") 
    plt.bar(values, dist)
    plt.show()


# def main(self):
#     print("hello")

if __name__ == "__main__":
    binomial(0.3, 10)