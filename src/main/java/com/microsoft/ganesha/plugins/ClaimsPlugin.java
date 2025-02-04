package com.microsoft.ganesha.plugins;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.microsoft.ganesha.models.Claim;
import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;

public class ClaimsPlugin {
    @DefineKernelFunction(name = "getClaims", description = "Gets recent and upcoming activities relating to the caller")
    public List<Claim> getClaims(String claimId) {
        List<Claim> claims = Arrays.asList(
                new Claim("C001", "P001", "John Doe", new Date(), 1500.00, "Pending"),
                new Claim("C002", "P002", "Jane Smith", new Date(), 2500.00, "Approved"),
                new Claim("C003", "P003", "Alice Johnson", new Date(), 3000.00, "Rejected"),
                new Claim("C004", "P004", "Bob Brown", new Date(), 1200.00, "Pending"),
                new Claim("C005", "P005", "Charlie Davis", new Date(), 1800.00, "Approved"));

        claims = claims.stream()
                .filter(claim -> claim.getClaimId().equals(claimId))
                .collect(Collectors.toList());

        return claims;
    }
}
