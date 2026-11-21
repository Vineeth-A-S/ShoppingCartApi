package com.shoppingcart.config;

import com.shoppingcart.model.Product;
import com.shoppingcart.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            List<Product> products = Arrays.asList(
                    new Product(
                            "Wireless Noise-Canceling Headphones",
                            "Premium active noise-canceling over-ear headphones with 40h battery life and spatial audio support.",
                            249.99,
                            "Audio",
                            15,
                            "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=600&q=80",
                            4.8
                    ),
                    new Product(
                            "Ultra-Slim Mechanical Keyboard",
                            "Low-profile mechanical switches, wireless Bluetooth 5.2 & 2.4GHz connectivity with customizable RGB.",
                            119.50,
                            "Accessories",
                            22,
                            "https://images.unsplash.com/photo-1587829741301-dc798b83add3?auto=format&fit=crop&w=600&q=80",
                            4.7
                    ),
                    new Product(
                            "Smart Fitness Watch Series 5",
                            "Track daily activity, heart rate, SPO2 and sleep cycles with brilliant AMOLED always-on display.",
                            179.99,
                            "Wearables",
                            18,
                            "https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=600&q=80",
                            4.6
                    ),
                    new Product(
                            "Ergonomic Wireless Precision Mouse",
                            "High-precision laser sensor with hyper-fast scroll wheel and ergonomic wrist comfort contour.",
                            69.99,
                            "Accessories",
                            30,
                            "https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?auto=format&fit=crop&w=600&q=80",
                            4.5
                    ),
                    new Product(
                            "4K Ultra-HD Portable Monitor 15.6\"",
                            "Lightweight travel companion with USB-C and HDMI input, 100% sRGB color gamut and dual speakers.",
                            289.00,
                            "Electronics",
                            8,
                            "https://images.unsplash.com/photo-1541807084-5c52b6b3adef?auto=format&fit=crop&w=600&q=80",
                            4.9
                    ),
                    new Product(
                            "High-Fidelity Studio Desk Speakers",
                            "Compact desktop monitor speakers delivering deep acoustic clarity, Bluetooth and RCA inputs.",
                            149.00,
                            "Audio",
                            12,
                            "https://images.unsplash.com/photo-1545454675-3531b543be5d?auto=format&fit=crop&w=600&q=80",
                            4.7
                    ),
                    new Product(
                            "Water-Resistant Commuter Backpack",
                            "Modern 25L city pack with dedicated padded laptop sleeve, hidden security pocket, and USB charging pass-through.",
                            79.99,
                            "Accessories",
                            25,
                            "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?auto=format&fit=crop&w=600&q=80",
                            4.6
                    ),
                    new Product(
                            "Smart Bedside Ambient Lamp",
                            "Touch-controlled color temperature gradient lamp compatible with voice assistants and gentle wake alarms.",
                            45.00,
                            "Home & Office",
                            14,
                            "https://images.unsplash.com/photo-1507473885765-e6ed057f782c?auto=format&fit=crop&w=600&q=80",
                            4.4
                    ),
                    new Product(
                            "Magnetic 3-in-1 Fast Wireless Charger",
                            "Simultaneously powers phone, smartwatch, and earbuds with fast inductive charging and minimal footprint.",
                            59.95,
                            "Electronics",
                            20,
                            "https://images.unsplash.com/photo-1583863788434-e58a36330cf0?auto=format&fit=crop&w=600&q=80",
                            4.5
                    ),
                    new Product(
                            "Minimalist Desk Pad & Mouse Mat",
                            "Large waterproof vegan leather desk blotter for clean desk aesthetics and smooth glide.",
                            29.99,
                            "Home & Office",
                            35,
                            "https://images.unsplash.com/photo-1616440347437-b1c73416efc2?auto=format&fit=crop&w=600&q=80",
                            4.7
                    )
            );
            productRepository.saveAll(products);
            System.out.println(">>> Sample product catalog seeded successfully! Total items: " + products.size());
        }
    }
}
