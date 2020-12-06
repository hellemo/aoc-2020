testdata = Parse.(Int,split(read(joinpath(@__DIR__,"input.txt"),String)));

dec01_1(x) = first(i*j for i ∈ x, j ∈ x if i+j == 2020)

dec01_2(x) = first(i*j*k for i ∈ x, j ∈ x, k ∈ x if i+j+k == 2020)

using Test
@test dec01_1(testdata) == 259716
@test dec01_2(testdata) == 120637440